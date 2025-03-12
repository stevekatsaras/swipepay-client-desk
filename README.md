client-desk
-------------

In production, we will need to pass the following as environment variables...

spring.datasource.url=jdbc:mariadb:replication//<MASTER_URL:PORT>,<SLAVE_URL:PORT>/swipepay
spring.datasource.password=<16_CHARACTER_GENERATED>
spring.jpa.show-sql=false

client-desk.management.password=$2y$10$8U3bbp/hJUxNmm1dFYF/SOtQ7yy7PCkIZJex9iBF6mMxg4wc8Q9gS # m@n@g3ment (in bcrypt)

crypto-service-client.root-uri: <URL>
crypto-service-client.connect-timeout: 15000 # 15 seconds
crypto-service-client.read-timeout: 15000 # 15 seconds
crypto-service-client.username: <USERNAME>
crypto-service-client.password: <PASSWORD>






For more information on configuring the MariaDB JDBC url, check this link:
https://mariadb.com/kb/en/mariadb/about-mariadb-connector-j/

For more information on configuring the tomcat datasource pool, check this link:
http://www.tomcatexpert.com/blog/2010/04/01/configuring-jdbc-pool-high-concurrency

TODO: implement 'change plan'
TODO: finish the 'billing page(s)'
TODO: implement XERO integration!
TODO: should implement a little card lookup page (for merchants to evaluate a customers' card)





