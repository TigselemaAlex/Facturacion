package com.capibaracode.backend.util.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "factura")
@Getter
@Setter
public class InvoiceXMLModel {

    private InfoTributaria infoTributaria;

    @Getter
    @Setter
    public static class InfoTributaria {
        private String ambiente;
        private String tipoEmision;
        private String razonSocial;
        private String nombreComercial;
        private String ruc;
        private String claveAcceso;
        private String codDoc;
        private String estab;
        private String ptoEmi;
        private String secuencial;
        private String dirMatriz;
    }

    private InfoFactura infoFactura;

    @Getter
    @Setter
    public static class InfoFactura{
        private String fechaEmision;
        private String dirEstablecimiento;
        private String contribuyenteEspecial;
        private String obligadoContabilidad;
        private String tipoIdentificacionComprador;
        private String guiaRemision;
        private String razonSocialComprador;
        private String identificacionComprador;
        private String direccionComprador;
        private String totalSinImpuestos;
        private String totalDescuento;
        private TotalConImpuestos totalConImpuestos;
        @Getter
        @Setter
        public static class TotalConImpuestos{
            @JacksonXmlElementWrapper(useWrapping = false)
            private List<TotalImpuesto> totalImpuesto = new ArrayList<>();
            @Getter
            @Setter
            public static class TotalImpuesto{
                private String codigo;
                private String codigoPorcentaje;
                private String descuentoAdicional;
                private String baseImponible;
                private String valor;
            }
        }
        private String propina;
        private String importeTotal;
        private String moneda;
        private Pagos pagos;
        @Getter
        @Setter
        public static class Pagos{
            @JacksonXmlElementWrapper(useWrapping = false)
            private List<Pago> pago = new ArrayList<>();
            @Getter
            @Setter
            public static class Pago{
                private String formaPago;
                private String total;
                private String plazo;
                private String unidadTiempo;
            }

        }
    }

    private Detalles detalles;

    @Getter
    @Setter
    public static class Detalles{
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Detalle> detalle = new ArrayList<>();
        @Getter
        @Setter
        public static class Detalle{
            private String codigoPrincipal;
            private String descripcion;
            private String cantidad;
            private String precioUnitario;
            private String descuento;
            private String precioTotalSinImpuesto;
            private Impuestos impuestos;
            @Getter
            @Setter
            public static class Impuestos{
                @JacksonXmlElementWrapper(useWrapping = false)
                private List<Impuesto> impuesto = new ArrayList<>();
                @Getter
                @Setter
                public static class Impuesto{
                    private String codigo;
                    private String codigoPorcentaje;
                    private String tarifa;
                    private String baseImponible;
                    private String valor;
                }
            }
        }
    }

}
