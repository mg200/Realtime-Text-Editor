FROM nginx
# # Remove the default Nginx configuration file
# RUN rm /usr/share/nginx/html/*
COPY info.conf /etc/nginx/conf.d/default.conf
EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]