#FROM php:7
#RUN apt-get update -y && apt-get install -y openssl zip unzip libonig-dev git nginx
#RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer
#RUN docker-php-ext-install pdo mbstring
FROM php7.4-nginx
RUN mkdir /app
WORKDIR /app
COPY . /app
COPY nginx.conf /etc/nginx/nginx.conf
RUN composer install --ignore-platform-reqs
COPY www.conf /etc/php/7.4/fpm/pool.d/
EXPOSE 80
#RUN service php7.4-fpm start && service nginx restart
#CMD ["nginx", "-g", "daemon off;"]
CMD service php7.4-fpm restart && service nginx restart && sleep infinity
