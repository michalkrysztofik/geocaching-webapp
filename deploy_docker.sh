#!/bin/sh
# Locally build docker container, push it and then deploy to VM by pulling from repo
# https://cloud.google.com/artifact-registry/docs/docker/store-docker-container-images

# gcloud auth login
# gcloud auth configure-docker europe-central2-docker.pkg.dev
# gcloud artifacts repositories create geocaching-webapp --repository-format=docker \
# --location=europe-central2 \
# --project=silken-glyph-365213

echo "Please provide your MongoDB credentials"
read -p "Enter database user: " DB_USER
read -s -p "Enter database password: " DB_PASS
echo
if [ -z "DB_USER" ] || [ -z "DB_PASS" ]; then
  echo
  echo "Error: both username and password are required"
  exit  1
fi

#gcloud compute addresses delete geocaching-webapp --region=europe-central2 --project=silken-glyph-365213 --quiet
gcloud compute addresses create geocaching-webapp \
 --region=europe-central2 \
 --network-tier=STANDARD \
 --project=silken-glyph-365213
STATIC_IP=$(gcloud compute addresses list --filter="name=geocaching-webapp" --format="value(address)" --project=silken-glyph-365213)

docker build . -t europe-central2-docker.pkg.dev/silken-glyph-365213/geocaching-webapp/latest
docker push europe-central2-docker.pkg.dev/silken-glyph-365213/geocaching-webapp/latest

gcloud compute instances delete geocaching-webapp-docker --zone=europe-central2-c --project=silken-glyph-365213 --quiet
gcloud compute instances create-with-container geocaching-webapp-docker \
 --container-image=europe-central2-docker.pkg.dev/silken-glyph-365213/geocaching-webapp/latest \
 --container-env="DB_USER=$DB_USER,DB_PASS=$DB_PASS" \
 --scopes=cloud-platform \
 --machine-type="e2-medium" \
 --network-interface "network-tier=STANDARD,address=geocaching-webapp" \
 --zone=europe-central2-c \
 --project=silken-glyph-365213 \

gcloud compute instances add-tags geocaching-webapp-docker --tags=webapp --zone=europe-central2-c --project=silken-glyph-365213
# will return error if rule already exist - can be ignored
gcloud compute firewall-rules create webapp-rule \
 --source-ranges=0.0.0.0/0 \
 --target-tags=webapp \
 --allow=tcp:8080 \
 --project=silken-glyph-365213

echo http://$(gcloud compute instances describe geocaching-webapp-docker \
 --format='value(networkInterfaces.accessConfigs[0].natIP)' --zone=europe-central2-c --project=silken-glyph-365213):8080