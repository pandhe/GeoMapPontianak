package com.pontianak.geomappontianak.modal;

public class model_list_metadata {
    boolean checked;
    String nama_meta,publisher_meta,link_meta,layer_Style;

    public model_list_metadata(boolean checked, String nama_meta, String publisher_meta, String link_meta, String layer_Style) {
        this.checked = checked;
        this.nama_meta = nama_meta;
        this.publisher_meta = publisher_meta;
        this.link_meta = link_meta;
        this.layer_Style = layer_Style;
    }

    @Override
    public String toString() {
        return nama_meta;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getPublisher_meta() {
        return publisher_meta;
    }

    public String getLink_meta() {
        return link_meta;
    }

    public String getLayer_Style() {
        return layer_Style;
    }
}
