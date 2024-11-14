FROM node:20 as build

# Set the working directory
WORKDIR /app

# Copy environment variable files
COPY .env.production .env

# Copy package.json and package-lock.json (for npm ci)
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code to the container
COPY . .

# Build the React application
RUN npm run build

# Stage 2 - Production Nginx image
FROM nginx:alpine

# Copy built React app to Nginx's html directory
COPY --from=build /app/build /usr/share/nginx/html