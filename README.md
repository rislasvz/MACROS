@echo off

SET BATCH_DIR=%~dp0

REM "productosGTB.jar"
SET JAR_FILE=productosGTB-1.0.jar

START "productos" javaw -jar "%BATCH_DIR%%JAR_FILE%"

EXIT
boolean formatValid = value.matches("[a-zA-Z0-9 ñÑáéíóúÁÉÍÓÚüÜ.]*");
