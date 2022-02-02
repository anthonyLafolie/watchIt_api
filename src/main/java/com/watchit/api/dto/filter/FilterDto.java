package com.watchit.api.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterDto {
    String name;
    boolean checked;

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || !this.getClass().equals(obj.getClass()))
            return false;
        FilterDto other = (FilterDto) obj;
        if (name.equals(other.getName()) && checked == other.isChecked())
            return true;
        return false;
    }
}
