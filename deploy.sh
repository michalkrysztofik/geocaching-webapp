#!/bin/sh
# Create a VM then deploy your application to the VM
# https://spring-gcp.saturnism.me/getting-started/helloworld/compute-engine

#gcloud compute addresses delete geocaching-webapp --region=europe-central2 --project=silken-glyph-365213 --quiet
gcloud compute addresses create geocaching-webapp \
    --region=europe-central2 \
    --network-tier=STANDARD \
    --project=silken-glyph-365213
STATIC_IP=$(gcloud compute addresses list --filter="name=geocaching-webapp" --format="value(address)" --project=silken-glyph-365213)

./gradlew clean bootJar
# `gcloud services enable compute.googleapis.com` if not enabled
gcloud compute instances stop geocaching-webapp --zone=europe-central2-c --project=silken-glyph-365213
# deleting old instance may take some time
gcloud compute instances delete geocaching-webapp --zone=europe-central2-c --project=silken-glyph-365213 --quiet
gcloud compute instances create geocaching-webapp \
--scopes=cloud-platform \
--machine-type="e2-medium" \
--network-interface "network-tier=STANDARD,address=geocaching-webapp" \
--zone=europe-central2-c \
--project=silken-glyph-365213
gcloud compute instances add-tags geocaching-webapp --tags=webapp --zone=europe-central2-c --project=silken-glyph-365213
# will return error if rule already exist - can be ignored
gcloud compute firewall-rules create webapp-rule \
  --source-ranges=0.0.0.0/0 \
  --target-tags=webapp \
  --allow=tcp:8080 \
  --project=silken-glyph-365213
# need to wait a while before VM starts accepting connection
sleep 15
gcloud compute scp build/libs/geocaching-webapp.jar geocaching-webapp:~/geocaching-webapp.jar --zone=europe-central2-c --project=silken-glyph-365213

gcloud compute ssh geocaching-webapp --zone=europe-central2-c --project=silken-glyph-365213 \
  --command="sudo apt-get update && sudo apt-get install -y openjdk-17-jdk && nohup java -jar geocaching-webapp.jar &" &
echo http://$(gcloud compute instances describe geocaching-webapp \
  --format='value(networkInterfaces.accessConfigs[0].natIP)' --zone=europe-central2-c --project=silken-glyph-365213):8080
