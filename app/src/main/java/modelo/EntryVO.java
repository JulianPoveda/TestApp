package modelo;

/**
 * Created by Julian Poveda on 16/03/2016.
 */
public class EntryVO {
    private String  idAplicacion;
    private String  nombre;
    private String  linkImagen;
    private String  imagenB64;
    private String  resumen;
    private String  precio;
    private String  derechos;
    private String  artista;
    private String  linkDescarga;
    private String  categoria;


    public EntryVO(){

    }

    public EntryVO(String idAplicacion, String nombre, String linkImagen, String imagenB64, String resumen, String derechos, String precio, String artista, String linkDescarga, String categoria, String fechaLiberacion) {
        this.idAplicacion = idAplicacion;
        this.nombre = nombre;
        this.linkImagen = linkImagen;
        this.imagenB64 = imagenB64;
        this.resumen = resumen;
        this.derechos = derechos;
        this.precio = precio;
        this.artista = artista;
        this.linkDescarga = linkDescarga;
        this.categoria = categoria;
    }

    public String getIdAplicacion() {
        return idAplicacion;
    }

    public void setIdAplicacion(String idAplicacion) {
        this.idAplicacion = idAplicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLinkImagen() {
        return linkImagen;
    }

    public void setLinkImagen(String linkImagen) {
        this.linkImagen = linkImagen;
    }

    public String getImagenB64() {
        return imagenB64;
    }

    public void setImagenB64(String imagenB64) {
        this.imagenB64 = imagenB64;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getDerechos() {
        return derechos;
    }

    public void setDerechos(String derechos) {
        this.derechos = derechos;
    }

    public String getLinkDescarga() {
        return linkDescarga;
    }

    public void setLinkDescarga(String linkDescarga) {
        this.linkDescarga = linkDescarga;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
