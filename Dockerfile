# Etapa de construcción
FROM node:18-alpine AS build
WORKDIR /app

# Copia y instala solo las dependencias
COPY package*.json ./
RUN npm ci

# Instala Angular CLI y copia el resto del código
RUN npm install -g @angular/cli
COPY . .

# Construcción de la aplicación
RUN node --max_old_space_size=4096 ./node_modules/@angular/cli/bin/ng build --configuration=production

# Etapa de producción
FROM nginx:latest
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/volunteer-app/browser/ /usr/share/nginx/html/
EXPOSE 80



#docker build -t volunteer-app:production .
#docker run -d -p 8080:80 --name front-volunteerapp volunteer-app:production
#sudo nano /etc/nginx/sites-available/volunteerapp
#sudo systemctl restart nginx
