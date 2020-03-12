# Ejercitación C de Mercado Pago
Aplicación totalmente funcional que consume asincronicamente la API y tambien consulta asincronicamente las imágenes de bancos y tarjetas.
Tambien se valida correctamente que el usuario pueda solamente ingresar montos validos, positivos sin el cero, solo ingrese 7 dígitos enteros y 2 decimals (no se valida por la longitud del string, realmente se fija el número ingresado). Ademas se valida que se seleccione tarjetas de crédito activas (status) y que el monto ingresado sea permitido por esa tarjeta.
La técnia utilizada para el pasaje de información entre actividades es extendiendo de Binder para pasarlo por el Bundle del Intent. Se realizo una clase generic WrapperBinder para ser reutilizada para pasar cualquier tipo de información

