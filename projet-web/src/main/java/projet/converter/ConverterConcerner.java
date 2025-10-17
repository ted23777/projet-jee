package projet.converter;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import projet.jsf.data.Concerner;

@Named
@RequestScoped
public class ConverterConcerner implements Converter<Concerner> {

    @SuppressWarnings("unchecked")
    @Override
    public Concerner getAsObject(FacesContext context, UIComponent uic, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        List<Concerner> items = null;
        for (UIComponent c : uic.getChildren()) {
            if (c instanceof UISelectItems) {
                items = (List<Concerner>) ((UISelectItems) c).getValue();
                break;
            }
        }

        var idCulture = Integer.valueOf(value);  // Conversion de l'ID Culture
        var idEntretien = Integer.valueOf(value); // Conversion de l'ID Entretien
        for (Concerner item : items) {
            if (item.getIdCulture().equals(idCulture) && item.getIdEntretien().equals(idEntretien)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Concerner item) {
        if (item == null) {
            return "";
        }
        return String.valueOf(item.getIdCulture()) + "-" + String.valueOf(item.getIdEntretien());
    }
}
