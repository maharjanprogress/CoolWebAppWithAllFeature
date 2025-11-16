# Stage 1: Build the Angular application
FROM node:20-alpine AS builder

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --legacy-peer-deps

# Copy the rest of the application
COPY . .

# Build the Angular app for production
RUN npm run build -- --configuration production

# Stage 2: Serve the application with Nginx
FROM nginx:1.25-alpine

# Remove default nginx configuration
RUN rm -rf /usr/share/nginx/html/*

# Copy built Angular app from builder stage
# IMPORTANT: Changed from dist/sadiksha/browser to dist/sadiksha
COPY --from=builder /app/dist/sadiksha/browser /usr/share/nginx/html

# Copy custom nginx configuration for Angular routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
