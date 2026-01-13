# ---- 1. Step: Build Frontend ----
FROM node:24.12.0-alpine AS frontendbuilder
WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .
RUN NODE_ENV=production npm run orval:generate
RUN NODE_ENV=production npm run build

# ---- 2. Step: Run Frontend ----
FROM node:24.12.0-alpine
WORKDIR /app

COPY --from=frontendbuilder /app ./

EXPOSE 3000
ENTRYPOINT ["npm", "run", "start" ]