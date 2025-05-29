init:
	sed -i 's/mode: never/mode: always/' ./src/main/resources/application.yml
	docker-compose -f deploy/docker-compose.yml up -d
	sleep 5
	mvn spring-boot:run
	sed -i 's/mode: always/mode: never/' ./src/main/resources/application.yml

run:
	mvn spring-boot:run

clean:
	mvn clean

deploy:
	docker-compose -f ./deploy/docker-compose-prod.yml up -d --build
	nginx -t
	nginx -s reload

backup:
	tar -zvcf data.tar.gz data

restore:
	tar -zvxf data.tar.gz

.PHONY: init clean run deploy backup restore