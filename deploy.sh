#!/bin/sh
# Create a VM then deploy your application to the VM
# https://spring-gcp.saturnism.me/getting-started/helloworld/compute-engine

./gradlew bootJar
JAR_PATH=build/libs/geocaching-webapp.jar
# `gcloud services enable compute.googleapis.com` if not enabled
gcloud compute instances stop geocaching-webapp --zone=europe-central2-c --project=silken-glyph-365213
# deleting old instance may take some time
gcloud compute instances delete geocaching-webapp --zone=europe-central2-c --project=silken-glyph-365213 --quiet
gcloud compute instances create geocaching-webapp --scopes=cloud-platform --zone=europe-central2-c --project=silken-glyph-365213
gcloud compute instances add-tags geocaching-webapp --tags=webapp --zone=europe-central2-c --project=silken-glyph-365213
# will return error if rule already exist - can be ignored
gcloud compute firewall-rules create webapp-rule \
  --source-ranges=0.0.0.0/0 \
  --target-tags=webapp \
  --allow=tcp:8080 \
  --project=silken-glyph-365213
# need to wait a while before VM starts accepting connection
sleep 10
gcloud compute scp $JAR_PATH geocaching-webapp:~/geocaching-webapp.jar --zone=europe-central2-c --project=silken-glyph-365213

gcloud compute ssh geocaching-webapp --zone=europe-central2-c --project=silken-glyph-365213 \
  --command="sudo apt-get update && sudo apt-get install -y openjdk-17-jdk && nohup java -jar geocaching-webapp.jar &" &
echo http://$(gcloud compute instances describe geocaching-webapp \
  --format='value(networkInterfaces.accessConfigs[0].natIP)' --zone=europe-central2-c --project=silken-glyph-365213):8080
