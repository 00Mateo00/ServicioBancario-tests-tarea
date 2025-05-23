#  instalacion y configuracion de sonarQube
## Paso 1: Crear el archivo `docker-compose.yaml`

Este archivo define los servicios (SonarQube y PostgreSQL), las redes y los volúmenes necesarios para ejecutar la aplicación.

Crea un directorio vacío para este tutorial (por ejemplo, `sonarqube-docker`) y dentro de él, crea un archivo llamado `docker-compose.yaml`. Copia y pega el siguiente contenido:

```yaml
services:
  sonarqube:
    image: sonarqube:lts-community 
    expose:
      - 9000
    ports:
      - "127.0.0.1:9000:9000"
    restart: always
    container_name: sonarqube
    networks:
      - sonarnet 
    environment:
      - SONARQUBE_JDBC_URL=jdbc:postgresql://db:5432/sonar
      - SONARQUBE_JDBC_USERNAME=sonar
      - SONARQUBE_JDBC_PASSWORD=sonar
    volumes:
      - sonarqube_conf:/opt/sonarqube/conf
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_extensions:/opt/sonarqube/extensions
      - sonarqube_bundled-plugins:/opt/sonarqube/lib/bundled-plugins
  db:
    image: postgres:latest
    networks:
      - sonarnet 
    environment:
      - POSTGRES_USER=sonar
      - POSTGRES_PASSWORD=sonar
      - POSTGRES_DB=sonar 
    volumes:
      - postgresql:/var/lib/postgresql
      - postgresql_data:/var/lib/postgresql/data
networks:
  sonarnet: 
volumes:
  sonarqube_conf:
  sonarqube_data:
  sonarqube_extensions:
  sonarqube_bundled-plugins:
  postgresql: 
  postgresql_data: 
```

## Paso 2: Iniciar los servicios

Abre una terminal o línea de comandos, navega al directorio donde guardaste el archivo `docker-compose.yaml` y ejecuta el siguiente comando:

docker-compose up -d

Espera unos minutos a que los contenedores se inicien completamente, especialmente SonarQube que necesita inicializar la base de datos por primera vez.

Puedes verificar que los contenedores están corriendo con:

docker-compose ps

## Paso 3: Acceder a la interfaz de SonarQube

Una vez que los contenedores estén corriendo y SonarQube haya inicializado, puedes acceder a la interfaz web.

Abre tu navegador y ve a:

http://127.0.0.1:9000

Las credenciales de inicio de sesión predeterminadas son:

*   **Usuario:** `admin`
*   **Contraseña:** `admin`

## Paso 4: Crear un Proyecto en SonarQube

Ahora vamos a configurar SonarQube para que reciba el análisis de tu proyecto.

1.  Una vez dentro de SonarQube, haz clic en **Projects** en el menú superior (o navega directamente a `/projects`).
2.  Haz clic en el botón **Create Project**.
3.  Selecciona la opción **Manually** (Manual).
4.  En la página de creación manual del proyecto (`/projects/create?mode=manual`), te pedirá los siguientes datos:
    *   **Project display name:** El nombre legible del proyecto que aparecerá en la interfaz (ej: `Mi Proyecto Banco`).  `com.mateoossa.banco` es el nombre que yo usé
    *   **Project key:** Un identificador único para el proyecto (ej: `com.mateoossa.banco`).
    *   **Main branch name:** El nombre de la rama principal de tu repositorio (normalmente `main` ).
5.  Haz clic en el botón **Set up**.

## Paso 5: Analizar tu Proyecto Java con Maven

Después de crear el proyecto, SonarQube te guiará a través de la configuración del análisis.

1.  En la siguiente pantalla, selecciona **Locally** (Localmente).
2.  Se te pedirá que generes un token. Dale un nombre (ej: `análisis-ci`) y haz clic en **Generate**.
3.  Copia el token que se genera. **Este token solo se mostrará una vez, así que cópialo y guárdalo temporalmente.**
4.  Selecciona tu tecnología (Java) y tu constructor (Maven).
5.  SonarQube te mostrará un comando similar a este. Deberás ejecutarlo en la **raíz de tu proyecto Java**:

mvn clean verify sonar:sonar -Dsonar.projectKey=com.mateoossa.banco -Dsonar.host.url=http://127.0.0.1:9000 -Dsonar.login=tu_token

    *   Reemplaza `com.mateoossa.banco` con el **Project key** que definiste en el paso anterior.
    *   Asegúrate de que `http://127.0.0.1:9000` es la URL correcta de tu instancia de SonarQube.
    *   Reemplaza `tu_token` con el token que generaste en el paso anterior.

6.  Abre una terminal cmd, navega al directorio raíz de tu proyecto Java y ejecuta el comando modificado.


## Paso 6: Ver los Resultados del Análisis

Una vez que el comando `mvn` haya terminado exitosamente (busca mensajes como `ANALYSIS SUCCESSFUL`), los resultados se habrán enviado a tu instancia de SonarQube.

Vuelve a la interfaz de SonarQube en http://127.0.0.1:9000. Puedes ir a la página principal o a la sección **Projects**. Deberías ver tu proyecto listado con los resultados del análisis (calidad, deuda técnica, errores, vulnerabilidades, etc.).

Haz clic en el nombre de tu proyecto para ver los detalles del análisis.
## Limpieza (Opcional)

Si quieres detener y eliminar los contenedores y volúmenes (esto eliminará todos los datos de SonarQube y la DB), navega al directorio donde está `docker-compose.yaml` y ejecuta:

docker-compose down --volumes

Si solo quieres detenerlos sin eliminar datos, usa:

docker-compose down