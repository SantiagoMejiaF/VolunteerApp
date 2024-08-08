FROM node:latest AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
RUN npm install -g @angular/cli
COPY . .
RUN npm run build --configuration=production
FROM nginx:latest
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/volunteer-app /usr/share/nginx/html
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]

#docker build -t volunteer-app:production .
#docker run -d -p 8080:80 --name front-volunteerapp volunteer-app:production
