# Realtime Distributed Chat Application

A production-style realtime messaging platform built using **Spring Boot**, **React**, **PostgreSQL**, **Redis**, **WebSockets/STOMP**, **Docker**, and **AWS**.

The application is designed around realtime communication, distributed presence synchronization, secure authentication, scalable message retrieval, and production deployment practices.

---


### Repositories

- Frontend: https://github.com/Priynshu2341/Realtime-chat-app-frontend
- Backend: https://github.com/Priynshu2341/Realtime-chat-app-backend

---

# Features

## Authentication & Security

- JWT Authentication
- Access Token + Refresh Token architecture
- Secure API authorization using Spring Security
- Automatic token refresh workflow
- Protected REST endpoints
- Secure WebSocket authentication
- User-specific message delivery channels

---

## Realtime Messaging

- Instant messaging using WebSockets + STOMP
- Low-latency bidirectional communication
- Realtime message delivery
- Message delivery status updates
- Read/Seen receipts
- Multi-tab synchronization
- Failed message retry handling
- Realtime chat updates without polling

---

## Typing Indicators

- Realtime typing notifications
- Typing start/stop events
- Chat-specific typing subscriptions
- Automatic typing state synchronization

---

## Online Presence System

- Online/offline user tracking
- Presence updates broadcast in realtime
- Distributed presence synchronization
- Presence state shared across backend instances
- Automatic presence recovery

---

## Distributed Presence Architecture

Implemented using Redis.

### Redis Pub/Sub

Synchronizes user presence across multiple backend instances.

### Presence Heartbeat

Users periodically refresh their online state.

### Redis TTL Recovery

If a server crashes unexpectedly:

- stale online users are automatically removed
- presence remains consistent
- ghost users are prevented

---

## Cursor-Based Pagination

Messages are loaded using cursor pagination instead of OFFSET pagination.

Benefits:

- Better scalability
- Faster historical message retrieval
- Stable chronological ordering
- Efficient infinite scrolling
- Reduced database overhead

---

## Infinite Scroll

- Automatic historical message loading
- Scroll position preservation
- Optimized pagination requests
- Smooth user experience

---

## Media Messaging

### AWS S3 Integration

- Image uploads
- Cloud storage
- Secure file access
- Realtime image sharing

### Attachments

- Media previews
- Image message support
- Attachment synchronization

---

## Event-Driven Communication

The system uses an event-driven architecture built around:

- WebSockets
- STOMP messaging
- Redis Pub/Sub
- Realtime broadcasts

This allows:

- low latency updates
- efficient message propagation
- asynchronous communication workflows

---

## WebSocket Infrastructure

### STOMP Messaging

- Topic subscriptions
- User-specific queues
- Broadcast messaging
- Message routing

### Connection Recovery

- Automatic reconnect support
- Session recovery
- Subscription restoration
- Resilience against unstable networks

---

## Production Deployment

### Dockerized Infrastructure

Services containerized using Docker.

Includes:

- Spring Boot backend
- PostgreSQL
- Redis

### AWS EC2 Deployment

Application deployed on AWS EC2.

### Nginx Reverse Proxy

Configured:

- HTTPS routing
- WSS routing
- SSL termination
- Reverse proxy forwarding
- Secure WebSocket communication

### SSL Security

- HTTPS enabled
- WSS enabled
- Secure client-server communication

---

# Architecture

```text
                React Frontend
                       │
            REST APIs + WebSockets
                       │
                       ▼
              Spring Boot Backend
                       │
          ┌────────────┴────────────┐
          ▼                         ▼
     PostgreSQL                 Redis
   (Messages/Data)      (Presence + Pub/Sub)
```

---

# Tech Stack

## Backend

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- WebSocket
- STOMP
- JWT
- PostgreSQL
- Redis

## Frontend

- React
- Redux Toolkit
- React Router
- Axios
- SockJS
- STOMP.js

## DevOps

- Docker
- Docker Compose
- AWS EC2
- AWS S3
- Nginx
- Linux

---

# Key Engineering Challenges Solved

### Secure WebSocket Authentication

Implemented JWT authentication flow for realtime websocket communication.

### Distributed Presence Synchronization

Built Redis Pub/Sub presence architecture that supports multiple backend instances.

### Fault-Tolerant Presence Tracking

Used Redis TTL heartbeat recovery to automatically clean stale online users.

### Scalable Message Retrieval

Implemented cursor pagination and infinite scrolling for efficient historical message loading.

### Production HTTPS/WSS Deployment

Configured SSL certificates, Nginx reverse proxying, websocket forwarding, and cloud deployment infrastructure.

### Infrastructure Debugging

Solved production issues involving:

- Docker networking
- SSL configuration
- WebSocket proxying
- Reverse proxy routing
- AWS deployment
- Redis connectivity

---


# Future Improvements

- Group Chats
- Push Notifications
- Voice Calling
- Video Calling
- Message Reactions
- End-to-End Encryption
- Message Search
- Chat Backup & Restore
- Kubernetes Deployment
- Monitoring & Observability

---

# What I Learned

Through this project I gained hands-on experience with:

- Realtime System Design
- WebSocket Communication
- STOMP Messaging
- Redis Pub/Sub
- Distributed Presence Synchronization
- JWT Authentication
- Cursor Pagination
- Event-Driven Architecture
- Dockerized Deployments
- AWS Infrastructure
- Nginx Reverse Proxying
- SSL/WSS Configuration
- Production Debugging
- Backend Scalability Concepts