# Build
FROM node:18-alpine AS build
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN npx ng build --configuration=production

# Prod
FROM nginx:latest
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/volunteer-app/browser/ /usr/share/nginx/html/


EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

#docker build -t volunteer-app:production .
#docker run -d -p 8080:80 --name front-volunteerapp volunteer-app:production
#sudo nano /etc/nginx/sites-available/volunteerapp
#sudo systemctl restart nginx
