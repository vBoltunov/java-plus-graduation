# Explore With Me - Дипломный проект

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.9.0-6DB33F?logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2023.0.3-6DB33F?logo=spring)
![Hibernate](https://img.shields.io/badge/Hibernate-6.5.2.Final-59666C?logo=hibernate)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-3.6+-231F20?logo=apachekafka)
![gRPC](https://img.shields.io/badge/gRPC-1.60+-4285F4?logo=grpc)
![Docker](https://img.shields.io/badge/Docker-20.10+-2496ED?logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.9-C71A36?logo=apache-maven&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-1.18.32-green?logo=lombok)
![MapStruct](https://img.shields.io/badge/MapStruct-1.5.5.Final-red)

## 📋 Краткое описание
Дипломная версия сервиса афиши мероприятий, переработанная для современных облачных сред. Основные улучшения: переход на микросервисную архитектуру, внедрение рекомендательной системы и интеграция потоковой обработки данных.

## 🏗️ Архитектура

### Инфраструктурные сервисы (`infra` модуль)
- **Eureka Server** - сервис обнаружения и регистрации микросервисов
- **API Gateway** - единая точка входа с маршрутизацией запросов
- **Config Server** - централизованное управление конфигурациями

### Бизнес-сервисы (`core` модуль)
- **Event Service** - управление мероприятиями
- **Request Service** - управление заявками на участие
- **User Service** - управление пользователями
- **Subscription Service** - управление подписками на пользователей
- **Interaction API** - общие DTO, Feign-клиенты, обработчики исключений, константы

### Рекомендательная система (`stats` модуль)
- **Collector** - сбор данных о действиях пользователей через gRPC
- **Aggregator** - расчет сходства мероприятий с использованием Apache Kafka
- **Analyzer** - генерация персональных рекомендаций
- **Serialization** - Avro и Proto схемы для сериализации данных
- **Stats Client** - gRPC-клиенты для взаимодействия с Collector и Analyzer

## 🛠️ Технологический стек
- **Backend**: Java 21, Spring Boot 3.9, Spring Cloud 2023
- **Базы данных**: PostgreSQL 16.1
- **Очереди сообщений**: Apache Kafka 3.6+
- **Межсервисное взаимодействие**: gRPC, Spring Cloud OpenFeign
- **Сериализация**: Apache Avro, Protocol Buffers
- **Контейнеризация**: Docker, Docker Compose
- **Сборка**: Apache Maven 3.9.9
- **Вспомогательные**: Lombok, MapStruct
- **Алгоритмы**: Косинусное сходство для рекомендательной системы

## ✨ Ключевые изменения и новые возможности

### Новые функциональные возможности
1. **Персональные рекомендации** - `GET /events/recommendations`
2. **Оценка мероприятий** - `PUT /events/{id}/like` (лайки)
3. **Расширенная аналитика** - автоматический сбор пользовательских взаимодействий

### Архитектурные улучшения
- **Микросервисная архитектура** - разделение монолита на 5 независимых бизнес-сервисов
- **Событийно-ориентированная архитектура** - асинхронная обработка данных через Apache Kafka
- **Облачная инфраструктура** - Spring Cloud Config, Eureka, Gateway
- **Высокопроизводительная связь** - gRPC для рекомендательной системы
- **Централизованная конфигурация** - единое управление настройками всех сервисов

## 🔄 Поток данных
1. **Сбор действий**: Пользовательские взаимодействия → gRPC → Collector → Kafka
2. **Агрегация**: Kafka → Aggregator → Расчет косинусного сходства → Kafka  
3. **Анализ и хранение**: Kafka → Analyzer → База данных рекомендаций
4. **Выдача рекомендаций**: Запрос от сервиса → gRPC → Analyzer → Персональные рекомендации

## 🔗 Ссылки
- **Исходный групповой проект**: [Explore With Me Plus](https://github.com/vBoltunov/java-explore-with-me-plus)

---
*Проект выполнен в рамках дипломной работы по курсу Java-разработки*
