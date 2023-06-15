#jenkins打包使用
FROM openjdk
EXPOSE 9544
VOLUME /tmp
ADD api/target/api.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+8","-jar","/app.jar"]
