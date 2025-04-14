version: "3.8"

services:
  abdellaouioussema-4twin2-g6-kaddem:
    image: abdellaouioussema-g2-4twin2:latest
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8090:8089"  # Map container port 8090 to host port 8089
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/kaddem?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=root
    depends_on:
      - db  # Make sure db service is ready before starting this one
    networks:
      - app-network  # Define network for communication between containers

  db:
    image: mysql:latest
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: kaddem  # Database name the app will use
    ports:
      - "3306:3306"  # Map container port 3306 to host port 3306
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-proot"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - app-network  # Use the same network to allow communication
networks:
  app-network:
    driver: bridge  # Define custom network for inter-container communication
