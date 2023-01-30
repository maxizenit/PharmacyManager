package ru.stepanov.pharmacymanager.util.xmlhandler;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import ru.stepanov.pharmacymanager.entity.Disease;

/**
 * Обработчик XML для болезней.
 */
public class DiseaseXMLHandler extends XMLHandler<Disease> {

  private static final String ENTITY_NAME = "disease";

  private static final String NAME_ATTR = "name";

  public DiseaseXMLHandler() {
    super(ENTITY_NAME);
  }

  @Override
  protected Disease createEntityFromAttrs(NamedNodeMap attrs) {
    Disease disease = new Disease();
    disease.setName(attrs.getNamedItem(NAME_ATTR).getNodeValue());
    return disease;
  }

  @Override
  protected void setAttrsFromEntity(Element element, Disease entity) {
    element.setAttribute(NAME_ATTR, entity.getName());
  }
}
