package projet.converter;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

import projet.jsf.data.Contenir;

@Named
@RequestScoped
public class ConverterContenir implements Converter<Contenir> {

    @SuppressWarnings("unchecked")
    @Override
    public Contenir getAsObject(FacesContext context, UIComponent uic, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        List<Contenir> items = null;
        for (UIComponent c : uic.getChildren()) {
            if (c instanceof UISelectItems) {
                items = (List<Contenir>) ((UISelectItems) c).getValue();
                break;
            }
        }

        // Découper la valeur "idParcelle-idCulture"
        String[] parts = value.split("-");
        if (parts.length != 2) {
            return null;
        }
        
        var idParcelle = Integer.valueOf(parts[0]);  // Exemples de conversion des valeurs pour `Contenir`
        var idCulture = Integer.valueOf(parts[0]);
        for (Contenir item : items) {
            if (item.getIdParcelle().equals(idParcelle) && item.getIdCulture().equals(idCulture)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Contenir item) {
        if (item == null) {
            return "";
        }
        return String.valueOf(item.getIdParcelle()) + "-" + String.valueOf(item.getIdCulture());
    }
}
