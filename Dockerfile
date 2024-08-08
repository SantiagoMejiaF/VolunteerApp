# Etapa de construcción
FROM node:latest AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
RUN npm install -g @angular/cli
COPY . .
RUN ng build --configuration=production

# Etapa de producción
FROM nginx:latest
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/volunteer-app/browser/ /usr/share/nginx/html/
EXPOSE 80


#docker build -t volunteer-app:production .
#docker run -d -p 8080:80 --name front-volunteerapp volunteer-app:production
