package projet.converter;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import projet.jsf.data.Mouvement;

@Named
@RequestScoped
public class ConverterMouvement implements Converter<Mouvement> {

    @SuppressWarnings("unchecked")
    @Override
    public Mouvement getAsObject(FacesContext context, UIComponent uic, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        List<Mouvement> items = null;
        for (UIComponent c : uic.getChildren()) {
            if (c instanceof UISelectItems) {
                items = (List<Mouvement>) ((UISelectItems) c).getValue();
                break;
            }
        }

        var id = Integer.valueOf(value);
        for (Mouvement item : items) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Mouvement item) {
        if (item == null) {
            return "";
        }
        return String.valueOf(item.getId());
    }
}
