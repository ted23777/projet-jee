package projet.converter;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import projet.jsf.data.Culture;

@Named
@RequestScoped
public class ConverterCulture implements Converter<Culture> {

    @SuppressWarnings("unchecked")
    @Override
    public Culture getAsObject(FacesContext context, UIComponent uic, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        List<Culture> items = null;
        for (UIComponent c : uic.getChildren()) {
            if (c instanceof UISelectItems) {
                items = (List<Culture>) ((UISelectItems) c).getValue();
                break;
            }
        }

        var id = Integer.valueOf(value);
        for (Culture item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Culture item) {
        if (item == null) {
            return "";
        }
        return String.valueOf(item.getId());
    }
}
