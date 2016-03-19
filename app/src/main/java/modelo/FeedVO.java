package modelo;

/**
 * Created by Julian Poveda on 16/03/2016.
 */
public class FeedVO {
    private String autor;
    private String fechaActualizacion;
    private String derechos;
    private String titulo;


    public FeedVO(String autor, String fechaActualizacion, String derechos, String titulo) {
        this.autor = autor;
        this.fechaActualizacion = fechaActualizacion;
        this.derechos = derechos;
        this.titulo = titulo;
    }


    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getDerechos() {
        return derechos;
    }

    public void setDerechos(String derechos) {
        this.derechos = derechos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
