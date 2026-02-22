keytool -importcert -alias <nombre> -file <certificado.crt> -keystore <truststore.jks


Ejemplo práctico:
keytool -importcert -alias micert -file certificado.crt -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit
La contraseña por defecto del cacerts de Java es changeit.



# Listar certificados en el truststore
keytool -list -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit

# Listar con detalles
keytool -list -v -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit

# Eliminar un certificado
keytool -delete -alias <nombre> -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit

# Ver detalles de un certificado .crt
keytool -printcert -file certificado.crt
