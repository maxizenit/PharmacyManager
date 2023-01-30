package ru.stepanov.pharmacymanager.util.report;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.stepanov.pharmacymanager.dto.MedicineNumberSalesDTO;

/**
 * Генератор HTML-отчёта JasperReports для количества продаж лекарств.
 */
public class MedicineNumberSalesReportGenerator extends
    ReportGenerator<MedicineNumberSalesDTO, MedicineNumberSalesDataBean> {

  /**
   * Файл с шаблоном отчёта.
   */
  private static final Resource REPORT_PATTERN = new ClassPathResource(
      "medicinenumbersalesreport.jrxml");

  public MedicineNumberSalesReportGenerator() {
    super(REPORT_PATTERN);
  }

  @Override
  protected MedicineNumberSalesDataBean createDataBeanFromEntity(MedicineNumberSalesDTO entity) {
    MedicineNumberSalesDataBean bean = new MedicineNumberSalesDataBean();

    bean.setName(entity.getMedicine().getName());
    bean.setCount(entity.getCount().toString());

    return bean;
  }
}
