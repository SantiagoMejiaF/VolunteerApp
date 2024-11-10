# Build
FROM node:18-alpine AS build
WORKDIR /app

# Copiar los archivos de configuración de npm
COPY package*.json ./
RUN npm ci

# Copiar el código fuente
COPY . .

# Ejecutar la construcción de la aplicación Angular
RUN npx ng build --configuration=production

# Prod
FROM nginx:latest

# Copiar el archivo de configuración de nginx
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Crear las carpetas necesarias antes de copiar los archivos
RUN mkdir -p /usr/share/nginx/html

# Copiar los archivos generados por el build a la carpeta de Nginx
COPY --from=build /app/dist/volunteer-app/browser/ /usr/share/nginx/html/

# Exponer el puerto 80
EXPOSE 80

# Iniciar Nginx
CMD ["nginx", "-g", "daemon off;"]

#docker build -t volunteer-app:production .
#docker run -d -p 8080:80 --name front-volunteerapp volunteer-app:production
#sudo nano /etc/nginx/sites-available/volunteerapp
#sudo systemctl restart nginx
#npx ng build --configuration=production
#sudo cp -r dist/volunteer-app/browser/* /usr/share/nginx/html/
