package com.peliculas.recomendador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.peliculas.recomendador.repository")
public class Neo4jConfig {
    
    // Esta configuración permite que Spring Data Neo4j cargue las relaciones automáticamente
    // No necesitamos configurar nada más, SDN 6+ carga las relaciones por defecto
}