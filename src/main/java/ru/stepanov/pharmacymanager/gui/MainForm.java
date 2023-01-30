package ru.stepanov.pharmacymanager.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;
import ru.stepanov.pharmacymanager.dto.AmountByMonthDTO;
import ru.stepanov.pharmacymanager.dto.MedicineNumberSalesDTO;
import ru.stepanov.pharmacymanager.entity.Disease;
import ru.stepanov.pharmacymanager.entity.Medicine;
import ru.stepanov.pharmacymanager.entity.MedicinePurpose;
import ru.stepanov.pharmacymanager.entity.Order;
import ru.stepanov.pharmacymanager.entity.OrderDetail;
import ru.stepanov.pharmacymanager.gui.dialog.entitydialog.DiseaseDialog;
import ru.stepanov.pharmacymanager.gui.dialog.entitydialog.MedicineDialog;
import ru.stepanov.pharmacymanager.gui.entitytable.EntityTable;
import ru.stepanov.pharmacymanager.gui.entitytable.filter.MedicineScrollPaneTableFilter;
import ru.stepanov.pharmacymanager.gui.entitytable.model.AmountByMonthEntityTableModel;
import ru.stepanov.pharmacymanager.gui.entitytable.model.DiseaseEntityTableModel;
import ru.stepanov.pharmacymanager.gui.entitytable.model.MedicineEntityTableModel;
import ru.stepanov.pharmacymanager.gui.entitytable.model.MedicineNumberSalesEntityTableModel;
import ru.stepanov.pharmacymanager.gui.entitytable.model.OrderDetailEntityTableModel;
import ru.stepanov.pharmacymanager.gui.entitytable.model.OrderTableModel;
import ru.stepanov.pharmacymanager.service.DiseaseService;
import ru.stepanov.pharmacymanager.service.MedicinePurposeService;
import ru.stepanov.pharmacymanager.service.MedicineService;
import ru.stepanov.pharmacymanager.service.OrderDetailService;
import ru.stepanov.pharmacymanager.service.OrderService;
import ru.stepanov.pharmacymanager.util.calculator.AmountByMonthCalculator;
import ru.stepanov.pharmacymanager.util.calculator.MedicineNumberSalesCalculator;
import ru.stepanov.pharmacymanager.util.report.MedicineNumberSalesReportGenerator;
import ru.stepanov.pharmacymanager.util.xmlhandler.DiseaseXMLHandler;

@Component
public class MainForm extends JFrame {

  private final DiseaseService diseaseService;

  private final MedicinePurposeService medicinePurposeService;

  private final MedicineService medicineService;

  private final OrderDetailService orderDetailService;

  private final OrderService orderService;

  private final List<OrderDetail> currentOrderDetails = new ArrayList<>();

  private JPanel mainPanel;

  //Вкладка "Лекарства"
  private EntityTable<Medicine> medicineTable;

  private JButton addMedicineButton;

  private JButton editMedicineButton;

  private JButton deleteMedicineButton;

  private JComboBox<Disease> diseaseFilterField;

  private JTextField priceFromFilterField;

  private JTextField priceToFilterField;

  private JButton filterButton;

  private JButton clearFilterButton;

  private JButton addInOrderButton;

  private EntityTable<OrderDetail> currentOrderTable;

  private JButton deleteFromCurrentOrderButton;

  private JButton saveCurrentOrderButton;

  //Вкладка "Болезни"
  private EntityTable<Disease> diseaseTable;

  private JButton addDiseaseButton;

  private JButton editDiseaseButton;

  private JButton deleteDiseaseButton;

  private JButton loadFromXMLButton;

  private JButton saveToXMLButton;

  private JButton replaceFromXMLButton;

  //Вкладка "Назначения"
  private EntityTable<Medicine> medicinePurposeTable;

  private EntityTable<Disease> curableDiseasesTable;

  private EntityTable<Disease> otherDiseasesTable;

  private JButton deleteDiseaseFromPurposeButton;

  private JButton addDiseaseToPurposeButton;

  //Вкладка "История заказов"
  private EntityTable<Order> orderTable;

  private EntityTable<OrderDetail> orderDetailTable;

  //Вкладка "Статистика продаж"
  private EntityTable<MedicineNumberSalesDTO> medicineNumberSalesTable;

  private EntityTable<AmountByMonthDTO> amountByMonthTable;

  private JButton createReportButton;

  @Autowired
  public MainForm(DiseaseService diseaseService, MedicinePurposeService medicinePurposeService,
      MedicineService medicineService, OrderDetailService orderDetailService,
      OrderService orderService) {
    this.diseaseService = diseaseService;
    this.medicinePurposeService = medicinePurposeService;
    this.medicineService = medicineService;
    this.orderDetailService = orderDetailService;
    this.orderService = orderService;

    $$$setupUI$$$();
    initGUI();
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setContentPane(mainPanel);
    pack();
    setVisible(true);
  }

  private void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  private void createUIComponents() {
    medicineTable = new EntityTable<>(new MedicineEntityTableModel());
    currentOrderTable = new EntityTable<>(new OrderDetailEntityTableModel());
    diseaseTable = new EntityTable<>(new DiseaseEntityTableModel());
    medicinePurposeTable = new EntityTable<>(new MedicineEntityTableModel());
    curableDiseasesTable = new EntityTable<>(new DiseaseEntityTableModel());
    otherDiseasesTable = new EntityTable<>(new DiseaseEntityTableModel());
    orderTable = new EntityTable<>(new OrderTableModel());
    orderDetailTable = new EntityTable<>(new OrderDetailEntityTableModel());
    medicineNumberSalesTable = new EntityTable<>(new MedicineNumberSalesEntityTableModel());
    amountByMonthTable = new EntityTable<>(new AmountByMonthEntityTableModel());

    medicinePurposeTable.addMouseListener(new MouseClickedListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Medicine medicine = medicinePurposeTable.getSelectedEntity();

        if (medicine != null) {
          refreshCurableAndOthersDiseaseTables(medicine);
        }
      }
    });

    orderTable.addMouseListener(new MouseClickedListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Order order = orderTable.getSelectedEntity();

        if (order != null) {
          refreshOrderDetailTable(order);
        }
      }
    });
  }

  private void initGUI() {
    refreshMedicineTables();
    refreshDiseaseTable();
    refreshOrderTable();
    refreshMedicineNumberSalesTable();
    refreshAmountByMonthTable();

    addMedicineTableButtonListeners();
    addDiseaseTableButtonListeners();
    addMedicinePurposeTablesListeners();

    refreshDiseasesCombo();

    MedicineNumberSalesReportGenerator reportGenerator = new MedicineNumberSalesReportGenerator();
    JFileChooser chooser = new JFileChooser();

    createReportButton.addActionListener(a -> {
      List<MedicineNumberSalesDTO> dtoList = MedicineNumberSalesCalculator.calculate(
          orderDetailService.getAll());

      if (!dtoList.isEmpty()) {
        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
          try {
            reportGenerator.generate(chooser.getSelectedFile(), dtoList);
          } catch (JRException | IOException e) {
            showMessageDialog(e.getMessage());
          }
        }
      }
    });
  }

  private void refreshMedicineTables() {
    medicineTable.updateEntities(medicineService.getAll());
    medicinePurposeTable.updateEntities(medicineService.getAll());
  }

  private void refreshDiseaseTable() {
    diseaseTable.updateEntities(diseaseService.getAll());
  }

  private void refreshCurableAndOthersDiseaseTables(Medicine medicine) {
    List<Disease> otherDiseases = diseaseService.getAll();
    List<Disease> curableDiseases = medicinePurposeService.getAllDiseasesByMedicine(medicine);

    otherDiseases.removeAll(curableDiseases);

    curableDiseasesTable.updateEntities(curableDiseases);
    otherDiseasesTable.updateEntities(otherDiseases);
  }

  private void refreshOrderTable() {
    orderTable.updateEntities(orderService.getAll());
  }

  private void refreshOrderDetailTable(Order order) {
    orderDetailTable.updateEntities(orderDetailService.getOrderDetailsByOrder(order));
  }

  private void refreshMedicineNumberSalesTable() {
    medicineNumberSalesTable.updateEntities(
        MedicineNumberSalesCalculator.calculate(orderDetailService.getAll()));
  }

  private void refreshAmountByMonthTable() {
    amountByMonthTable.updateEntities(
        AmountByMonthCalculator.calculateAmountByMonths(orderDetailService.getAll()));
  }

  private void refreshDiseasesCombo() {
    DefaultComboBoxModel<Disease> diseaseModel = new DefaultComboBoxModel<>();
    diseaseModel.addAll(diseaseService.getAll());
    diseaseFilterField.setModel(diseaseModel);
  }

  private void addMedicineTableButtonListeners() {
    addMedicineButton.addActionListener(
        a -> new MedicineDialog(this, medicineService::save, this::refreshMedicineTables, null));

    editMedicineButton.addActionListener(a -> {
      Medicine medicine = medicineTable.getSelectedEntity();

      if (medicine != null) {
        new MedicineDialog(this, medicineService::save, this::refreshMedicineTables, medicine);
      }
    });

    deleteMedicineButton.addActionListener(a -> {
      Medicine medicine = medicineTable.getSelectedEntity();

      if (medicine != null) {
        medicineService.remove(medicine);
        refreshMedicineTables();
        curableDiseasesTable.clear();
        otherDiseasesTable.clear();
        currentOrderTable.clear();
        currentOrderDetails.clear();
      }
    });

    filterButton.addActionListener(a -> {
      MedicineScrollPaneTableFilter filter = new MedicineScrollPaneTableFilter(
          medicinePurposeService);

      try {
        filter.setDisease((Disease) diseaseFilterField.getSelectedItem());
        filter.setPriceFrom(
            StringUtils.hasText(priceFromFilterField.getText()) ? BigDecimal.valueOf(
                Double.parseDouble(priceFromFilterField.getText())) : null);
        filter.setPriceTo(StringUtils.hasText(priceToFilterField.getText()) ? BigDecimal.valueOf(
            Double.parseDouble(priceToFilterField.getText())) : null);

        medicineTable.filter(filter);
      } catch (NumberFormatException e) {
        showMessageDialog("Введите корректные числа в поля \"от\" и/или \"до\"");
      }
    });

    clearFilterButton.addActionListener(a -> {
      diseaseFilterField.setSelectedItem(null);
      priceFromFilterField.setText(null);
      priceToFilterField.setText(null);
    });

    addInOrderButton.addActionListener(a -> {
      Medicine medicine = medicineTable.getSelectedEntity();

      if (medicine != null) {
        Optional<OrderDetail> orderDetailWithMedicine = currentOrderDetails.stream()
            .filter(od -> od.getMedicine().equals(medicine)).findFirst();

        if (orderDetailWithMedicine.isPresent()) {
          orderDetailWithMedicine.get().setCount(orderDetailWithMedicine.get().getCount() + 1);
          orderDetailWithMedicine.get().setAmount(
              orderDetailWithMedicine.get().getMedicine().getPrice()
                  .multiply(BigDecimal.valueOf(orderDetailWithMedicine.get().getCount())));
        } else {
          OrderDetail orderDetail = new OrderDetail();
          orderDetail.setMedicine(medicine);
          orderDetail.setCount(1);
          orderDetail.setAmount(medicine.getPrice());
          currentOrderDetails.add(orderDetail);
        }

        currentOrderTable.updateEntities(currentOrderDetails);
      }
    });

    deleteFromCurrentOrderButton.addActionListener(a -> {
      OrderDetail orderDetail = currentOrderTable.getSelectedEntity();

      if (orderDetail != null) {
        OrderDetail orderDetailFromList = currentOrderDetails.stream()
            .filter(od -> od.getMedicine().equals(orderDetail.getMedicine())).findFirst().get();

        if (orderDetailFromList.getCount() > 1) {
          orderDetailFromList.setCount(orderDetailFromList.getCount() - 1);
          orderDetailFromList.setAmount(orderDetailFromList.getMedicine().getPrice()
              .multiply(BigDecimal.valueOf(orderDetailFromList.getCount())));
        } else {
          currentOrderDetails.remove(orderDetailFromList);
        }

        currentOrderTable.updateEntities(currentOrderDetails);
      }
    });

    saveCurrentOrderButton.addActionListener(a -> {
      if (!currentOrderDetails.isEmpty()) {
        Order order = new Order();
        order.setDate(new Date());

        orderService.save(order);
        currentOrderDetails.forEach(od -> {
          od.setOrder(order);
          orderDetailService.save(od);
        });

        currentOrderDetails.clear();
        currentOrderTable.clear();
        refreshMedicineTables();
        refreshOrderTable();
        refreshMedicineNumberSalesTable();
        refreshAmountByMonthTable();
      }
    });
  }

  private void addDiseaseTableButtonListeners() {
    addDiseaseButton.addActionListener(a -> {
      new DiseaseDialog(this, diseaseService::save, this::refreshDiseaseTable, null);
      refreshDiseasesCombo();
    });

    editDiseaseButton.addActionListener(a -> {
      Disease disease = diseaseTable.getSelectedEntity();

      if (disease != null) {
        new DiseaseDialog(this, diseaseService::save, this::refreshDiseaseTable, disease);
        refreshDiseasesCombo();
      }
    });

    deleteDiseaseButton.addActionListener(a -> {
      Disease disease = diseaseTable.getSelectedEntity();

      if (disease != null) {
        diseaseService.remove(disease);
        refreshDiseaseTable();
        refreshDiseasesCombo();
      }
    });

    JFileChooser chooser = new JFileChooser();
    DiseaseXMLHandler handler = new DiseaseXMLHandler();

    loadFromXMLButton.addActionListener(a -> {
      int result = chooser.showOpenDialog(this);

      if (result == JFileChooser.APPROVE_OPTION) {
        try {
          diseaseService.saveAll(handler.loadFromXML(chooser.getSelectedFile()));
          refreshDiseaseTable();
          refreshDiseasesCombo();
        } catch (IOException | SAXException e) {
          showMessageDialog(e.getMessage());
        }
      }
    });

    saveToXMLButton.addActionListener(a -> {
      int result = chooser.showSaveDialog(this);

      if (result == JFileChooser.APPROVE_OPTION) {
        handler.saveToXML(chooser.getSelectedFile(), diseaseService.getAll());
      }
    });

    replaceFromXMLButton.addActionListener(a -> {
      int result = chooser.showOpenDialog(this);

      if (result == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();
        List<Disease> diseases = new ArrayList<>();

        Thread removeThread = new Thread(diseaseService::removeAll);
        Thread loadThread = new Thread(() -> {
          try {
            diseaseService.saveAll(handler.loadFromXML(file));
          } catch (IOException | SAXException e) {
            SwingUtilities.invokeLater(() -> showMessageDialog(e.getMessage()));
          }
        });
        Thread saveThread = new Thread(() -> diseaseService.saveAll(diseases));

        try {
          removeThread.start();
          loadThread.start();

          removeThread.join();
          loadThread.join();

          saveThread.start();
          saveThread.join();
        } catch (InterruptedException e) {
          removeThread.interrupt();
          loadThread.interrupt();
          saveThread.interrupt();

          showMessageDialog(e.getMessage());
        } finally {
          refreshDiseaseTable();
          refreshDiseasesCombo();
        }
      }
    });
  }

  private void addMedicinePurposeTablesListeners() {
    deleteDiseaseFromPurposeButton.addActionListener(a -> {
      Medicine medicine = medicinePurposeTable.getSelectedEntity();
      Disease disease = curableDiseasesTable.getSelectedEntity();

      if (medicine != null && disease != null) {
        MedicinePurpose medicinePurpose = medicinePurposeService.getByMedicineAndDisease(medicine,
            disease);
        medicinePurposeService.remove(medicinePurpose);
        refreshCurableAndOthersDiseaseTables(medicine);
      }
    });

    addDiseaseToPurposeButton.addActionListener(a -> {
      Medicine medicine = medicinePurposeTable.getSelectedEntity();
      Disease disease = otherDiseasesTable.getSelectedEntity();

      if (medicine != null && disease != null) {
        MedicinePurpose medicinePurpose = new MedicinePurpose();
        medicinePurpose.setMedicine(medicine);
        medicinePurpose.setDisease(disease);
        medicinePurposeService.save(medicinePurpose);
        refreshCurableAndOthersDiseaseTables(medicine);
      }
    });
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.setPreferredSize(new Dimension(800, 600));
    final JTabbedPane tabbedPane1 = new JTabbedPane();
    mainPanel.add(tabbedPane1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
            new Dimension(200, 200), null, 0, false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Лекарства", panel1);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    panel2.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Фильтр",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    final JLabel label1 = new JLabel();
    label1.setText("Излечиваемая болезнь");
    panel2.add(label1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    diseaseFilterField = new JComboBox();
    panel2.add(diseaseFilterField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("Цена");
    panel2.add(label2,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
    panel2.add(panel3,
        new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setText("от");
    panel3.add(label3,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final JLabel label4 = new JLabel();
    label4.setText("до");
    panel3.add(label4,
        new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    priceFromFilterField = new JTextField();
    panel3.add(priceFromFilterField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    priceToFilterField = new JTextField();
    panel3.add(priceToFilterField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel2.add(panel4, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    filterButton = new JButton();
    filterButton.setText("Поиск");
    panel4.add(filterButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    clearFilterButton = new JButton();
    clearFilterButton.setText("Очистить");
    panel4.add(clearFilterButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel5 = new JPanel();
    panel5.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel5,
        new GridConstraints(1, 1, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel5.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Заказ",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    panel5.add(currentOrderTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JPanel panel6 = new JPanel();
    panel6.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel5.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    deleteFromCurrentOrderButton = new JButton();
    deleteFromCurrentOrderButton.setText("Удалить");
    panel6.add(deleteFromCurrentOrderButton,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    saveCurrentOrderButton = new JButton();
    saveCurrentOrderButton.setText("Оформить заказ");
    panel6.add(saveCurrentOrderButton,
        new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel7 = new JPanel();
    panel7.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    addMedicineButton = new JButton();
    addMedicineButton.setText("Добавить");
    panel7.add(addMedicineButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    editMedicineButton = new JButton();
    editMedicineButton.setText("Изменить");
    panel7.add(editMedicineButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    deleteMedicineButton = new JButton();
    deleteMedicineButton.setText("Удалить");
    panel7.add(deleteMedicineButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    addInOrderButton = new JButton();
    addInOrderButton.setText("В заказ");
    panel7.add(addInOrderButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    panel1.add(medicineTable,
        new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JPanel panel8 = new JPanel();
    panel8.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Болезни", panel8);
    panel8.add(diseaseTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JPanel panel9 = new JPanel();
    panel9.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel8.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    addDiseaseButton = new JButton();
    addDiseaseButton.setText("Добавить");
    panel9.add(addDiseaseButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    editDiseaseButton = new JButton();
    editDiseaseButton.setText("Изменить");
    panel9.add(editDiseaseButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    deleteDiseaseButton = new JButton();
    deleteDiseaseButton.setText("Удалить");
    panel9.add(deleteDiseaseButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    loadFromXMLButton = new JButton();
    loadFromXMLButton.setText("Загрузить из XML");
    panel9.add(loadFromXMLButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    saveToXMLButton = new JButton();
    saveToXMLButton.setText("Сохранить в XML");
    panel9.add(saveToXMLButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    replaceFromXMLButton = new JButton();
    replaceFromXMLButton.setText("Заменить из XML");
    panel9.add(replaceFromXMLButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel10 = new JPanel();
    panel10.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Назначения", panel10);
    final JPanel panel11 = new JPanel();
    panel11.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel10.add(panel11,
        new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
        "Болезни, от которых лечит лекарство", TitledBorder.DEFAULT_JUSTIFICATION,
        TitledBorder.DEFAULT_POSITION, null, null));
    panel11.add(curableDiseasesTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    deleteDiseaseFromPurposeButton = new JButton();
    deleteDiseaseFromPurposeButton.setText("Удалить");
    panel11.add(deleteDiseaseFromPurposeButton,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel12 = new JPanel();
    panel12.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel10.add(panel12,
        new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
        "Остальные болезни", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
        null, null));
    panel12.add(otherDiseasesTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    addDiseaseToPurposeButton = new JButton();
    addDiseaseToPurposeButton.setText("Добавить");
    panel12.add(addDiseaseToPurposeButton,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel13 = new JPanel();
    panel13.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel10.add(panel13,
        new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel13.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Лекарства",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    panel13.add(medicinePurposeTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JPanel panel14 = new JPanel();
    panel14.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("История заказов", panel14);
    panel14.add(orderTable,
        new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JPanel panel15 = new JPanel();
    panel15.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel14.add(panel15,
        new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel15.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
        "Список лекарств", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null,
        null));
    panel15.add(orderDetailTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JPanel panel16 = new JPanel();
    panel16.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Статистика продаж", panel16);
    final JPanel panel17 = new JPanel();
    panel17.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel16.add(panel17,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel17.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
        "Количество продаж конкретных лекарств", TitledBorder.DEFAULT_JUSTIFICATION,
        TitledBorder.DEFAULT_POSITION, null, null));
    panel17.add(medicineNumberSalesTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    createReportButton = new JButton();
    createReportButton.setText("Сформировать отчёт");
    panel17.add(createReportButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel18 = new JPanel();
    panel18.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel16.add(panel18,
        new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel18.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
        "Сумма продаж по месяцам", TitledBorder.DEFAULT_JUSTIFICATION,
        TitledBorder.DEFAULT_POSITION, null, null));
    panel18.add(amountByMonthTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }

}
