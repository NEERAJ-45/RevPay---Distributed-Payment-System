# RevPay CI/CD — Getting It Live (Step-by-Step)

> Follow these steps **in order**. Each section depends on the previous one.

---

## STEP 1 — Merge the CI/CD Branch into Master

Your pipeline code lives on `cicd/setup`. Get it into `master` first.

```bash
# On your local machine
git checkout master
git merge cicd/setup
git push origin master
```

> [!IMPORTANT]
> The 5 per-service workflows only trigger on pushes to `master`. They won't fire until this merge happens.

---

## STEP 2 — Make the GitHub Repo Public (or Upgrade Plan)

> [!IMPORTANT]
> **GitHub Actions** = unlimited free minutes on **public repos**.  
> **GHCR** = free image storage for **public repos**.  
> On a private repo, you get 2,000 min/month free and 500MB storage.  
> For a learning project, making it public is the easiest path.

Go to: **GitHub repo → Settings → Danger Zone → Change visibility → Public**

---

## STEP 3 — Create an Oracle Cloud Account

1. Go to **[cloud.oracle.com](https://cloud.oracle.com)** → **Start for free**
2. Register with email, phone, credit card *(for identity only — you will NOT be charged)*
3. **Choose Home Region**: pick `ap-mumbai-1` (India) or nearest region  
   ⚠️ **Cannot be changed later — choose carefully**
4. Complete verification and sign in to the OCI Console

---

## STEP 4 — Provision the Oracle Cloud VM

1. **Compute** → **Instances** → **Create Instance**
2. Set **Name**: `revpay-server`
3. Click **Change image** → Select **Canonical Ubuntu 22.04 LTS** → **Select image**
4. Click **Change shape**:
   - Family: **Ampere** (ARM)
   - Shape: `VM.Standard.A1.Flex`
   - OCPUs: **4** | Memory: **24 GB**
   - Click **Select shape**
5. Under **Add SSH keys** → **Generate a key pair for me** → **Save private key**  
   ⚠️ Save the `.key` file — **you cannot download it again**
6. Click **Create** — wait ~2 min for status → **RUNNING**
7. **Copy the Public IP address** — you'll need it in Step 8

---

## STEP 5 — Open Firewall Ports in OCI Console

OCI has two firewall layers. The VM setup script handles the OS layer — you must do the VCN layer manually.

1. **Networking** → **Virtual Cloud Networks** → click your VCN
2. **Security Lists** → **Default Security List** → **Add Ingress Rules**
3. Add one rule per row:

| Source CIDR | Protocol | Dest Port | Purpose |
|---|---|---|---|
| `0.0.0.0/0` | TCP | `22` | SSH |
| `0.0.0.0/0` | TCP | `80` | Nginx HTTP |
| `0.0.0.0/0` | TCP | `8080` | API Gateway |
| `0.0.0.0/0` | TCP | `8081` | User Service |
| `0.0.0.0/0` | TCP | `8082` | Wallet Service |
| `0.0.0.0/0` | TCP | `8083` | Transaction Service |
| `0.0.0.0/0` | TCP | `8084` | Notification Service |

4. Click **Add Ingress Rules**

---

## STEP 6 — Fix SSH Key Permissions & Connect

**Windows (PowerShell as Administrator):**
```powershell
icacls "$env:USERPROFILE\Downloads\ssh-key-*.key" /inheritance:r /grant:r "$env:USERNAME:R"
```

**Connect to the VM:**
```bash
ssh -i ~/Downloads/ssh-key-<date>.key ubuntu@<YOUR_OCI_PUBLIC_IP>
```

Test: you should see the Ubuntu shell prompt.

---

## STEP 7 — Run the Bootstrap Script on the VM

From your **local machine** (not inside the VM), run:

```bash
# Stream the bootstrap script directly over SSH
ssh -i ~/Downloads/ssh-key-<date>.key ubuntu@<YOUR_OCI_PUBLIC_IP> 'bash -s' \
  < infra/oci-setup.sh
```

This script automatically:
- ✅ Updates system packages
- ✅ Installs Docker + Docker Compose v2
- ✅ Opens Ubuntu UFW firewall ports
- ✅ Creates `/opt/revpay/` (permissions-locked directory)
- ✅ Creates `/opt/revpay/.env` template
- ✅ Creates the `upi-net` Docker bridge network

**Re-login after the script finishes** (for docker group to take effect):
```bash
# Exit and SSH back in
exit
ssh -i ~/Downloads/ssh-key-<date>.key ubuntu@<YOUR_OCI_PUBLIC_IP>
```

---

## STEP 8 — Fill In the Secrets File on the VM

```bash
# On the VM
nano /opt/revpay/.env
```

Replace every `REPLACE_ME`:

```env
POSTGRES_USER=revpay
POSTGRES_PASSWORD=<generate: openssl rand -base64 24>
POSTGRES_HOST=postgres
POSTGRES_PORT=5432

REDIS_HOST=redis
REDIS_PORT=6379
REDIS_PASSWORD=<generate: openssl rand -base64 24>

KAFKA_BOOTSTRAP_SERVERS=kafka:29092

JWT_SECRET=<generate: openssl rand -hex 64>
JWT_EXPIRY_MS=86400000

USER_SERVICE_PORT=8081
WALLET_SERVICE_PORT=8082
TRANSACTION_SERVICE_PORT=8083
NOTIFICATION_SERVICE_PORT=8084
GATEWAY_PORT=8080
```

Save: `Ctrl+O` → `Enter` → `Ctrl+X`

**Verify permissions** (must show `-rw-------`):
```bash
ls -la /opt/revpay/.env
```

> [!TIP]
> Generate strong secrets right on the VM:
> ```bash
> openssl rand -base64 24   # for passwords
> openssl rand -hex 64       # for JWT secret
> ```

---

## STEP 9 — Log In to GHCR on the VM

GitHub Container Registry needs a Personal Access Token (PAT) to pull your images.

**Create the PAT** (do this on GitHub in your browser):
1. GitHub → **Settings** → **Developer settings** → **Personal access tokens** → **Tokens (classic)**
2. **Generate new token (classic)**
3. Note: `revpay-oci-pull` | Expiry: `No expiration`
4. Scope: check only **`read:packages`**
5. Copy the token

**Login on the VM:**
```bash
echo "<YOUR_PAT_TOKEN>" | docker login ghcr.io -u <YOUR_GITHUB_USERNAME> --password-stdin
```

Expected: `Login Succeeded`

---

## STEP 10 — Copy docker-compose.yml and Start Infrastructure

```bash
# From your LOCAL machine — copy the compose file to the VM
scp -i ~/Downloads/ssh-key-<date>.key \
  docker-compose.yml ubuntu@<YOUR_OCI_PUBLIC_IP>:/opt/revpay/

# Also copy the infra directory (nginx, prometheus, grafana configs)
scp -i ~/Downloads/ssh-key-<date>.key -r \
  infra/ ubuntu@<YOUR_OCI_PUBLIC_IP>:/opt/revpay/
```

**SSH into VM and start infra:**
```bash
ssh -i ~/Downloads/ssh-key-<date>.key ubuntu@<YOUR_OCI_PUBLIC_IP>
cd /opt/revpay

# Remove the obsolete 'version:' key from docker-compose.yml
sed -i '/^version:/d' docker-compose.yml

# Start infrastructure services only (NOT the microservices — CI/CD deploys those)
docker compose --env-file .env up -d \
  postgres redis zookeeper kafka kafka-ui nginx prometheus grafana zipkin

# Verify all are running
docker compose --env-file .env ps
```

**Verify infra is healthy:**
```bash
# Postgres ready?
docker exec upi-postgres pg_isready -U revpay

# Redis ready?
docker exec upi-redis redis-cli -a <REDIS_PASSWORD> ping
# → PONG

# Kafka ready? (wait ~30s after starting)
docker exec upi-kafka kafka-broker-api-versions --bootstrap-server localhost:9092
```

---

## STEP 11 — Add GitHub Secrets

Go to: **GitHub repo → Settings → Secrets and variables → Actions → New repository secret**

Add each one:

| Secret Name | Value |
|---|---|
| `OCI_HOST` | Your VM's Public IP (e.g. `132.145.x.x`) |
| `OCI_USER` | `ubuntu` |
| `OCI_SSH_KEY` | **Full contents** of your `.key` file (include `-----BEGIN...-----` lines) |
| `POSTGRES_USER` | Same value as in `/opt/revpay/.env` |
| `POSTGRES_PASSWORD` | Same value as in `/opt/revpay/.env` |
| `REDIS_PASSWORD` | Same value as in `/opt/revpay/.env` |
| `JWT_SECRET` | Same value as in `/opt/revpay/.env` |

> [!IMPORTANT]
> For `OCI_SSH_KEY`: open your `.key` file in Notepad, select all, copy-paste the entire content including the header/footer lines.

---

## STEP 12 — Create the `production` GitHub Environment

The deploy jobs use `environment: production`. You need to create it.

1. GitHub → **Settings** → **Environments** → **New environment**
2. Name: `production`
3. (Optional but recommended) **Required reviewers** → add yourself → **Save protection rules**
4. Click **Save protection rules**

Without this step, the deploy job will fail with "environment not found".

---

## STEP 13 — Trigger Your First Deploy

```bash
# Make a small change to user-service to trigger only that pipeline
cd <your local project>

# Add a blank line to trigger the path filter
echo "" >> user-service/src/main/resources/application.properties

git add user-service/src/main/resources/application.properties
git commit -m "ci: trigger first OCI deploy for user-service"
git push origin master
```

**Watch it run:**  
Go to `github.com/<your-username>/RevPay---Distributed-Payment-System/actions`

You should see `CI/CD — User Service` workflow running with 3 jobs:

```
Job 1: Build & Test       ✅  (Maven + Testcontainers)
Job 2: Docker Build & Push ✅  (image pushed to GHCR)
Job 3: Deploy to OCI VM    ✅  (SSH → pull → run → health check)
```

---

## STEP 14 — Verify Everything On the VM

```bash
ssh -i ~/Downloads/ssh-key-<date>.key ubuntu@<YOUR_OCI_PUBLIC_IP>

# 1. Container is running?
docker ps | grep user-service

# 2. Health check passes?
curl -s http://localhost:8081/actuator/health | python3 -m json.tool

# 3. Secrets NOT visible in docker inspect args?
docker inspect user-service | grep -A3 '"Args"'
# Should be empty — secrets come from --env-file, not CLI args

# 4. Hit through Nginx (public access)?
curl http://<YOUR_OCI_PUBLIC_IP>/actuator/health
```

---

## Common Failure Points & Fixes

| Symptom | Fix |
|---|---|
| `Deploy` job: `SSH connection refused` | VCN port 22 not open — recheck Step 5 |
| `Deploy` job: `docker pull` fails | GHCR login expired on VM — re-run Step 9 |
| `Deploy` job: health check fails, rollback runs | Check `docker logs user-service` on VM — usually a missing env var |
| `Deploy` job: `environment 'production' not found` | Create the environment in GitHub (Step 12) |
| `upi-net` not found on VM | Run `docker network create upi-net` on VM |
| Service unreachable from browser | Recheck OCI VCN Security List (Step 5) |
| `Test` job fails | Your unit tests have a bug — fix locally and push again |

---

## Summary Checklist

```
[ ] STEP 1  — Merge cicd/setup → master
[ ] STEP 2  — Make repo public (or keep private and accept 2000 min/month limit)
[ ] STEP 3  — Create Oracle Cloud account
[ ] STEP 4  — Provision Ubuntu ARM VM (4 OCPU, 24GB)
[ ] STEP 5  — Open VCN firewall ports in OCI Console
[ ] STEP 6  — SSH into VM (verify connection works)
[ ] STEP 7  — Run infra/oci-setup.sh bootstrap
[ ] STEP 8  — Fill /opt/revpay/.env with real secrets
[ ] STEP 9  — docker login ghcr.io on the VM
[ ] STEP 10 — Copy docker-compose.yml, start infra stack
[ ] STEP 11 — Add 7 GitHub Secrets in repo settings
[ ] STEP 12 — Create 'production' GitHub Environment
[ ] STEP 13 — Push a change → watch pipeline run
[ ] STEP 14 — Verify container health on VM
```
