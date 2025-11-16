# 🎬 Sistema Recomendador de Películas - UADE

## 📁 Archivos del Proyecto

### ✅ Entidades (Model)
- `Pelicula.java` - peliculaId, titulo, año, promedioRating, duracion
- `Genero.java` - nombre
- `Actor.java` - nombre
- `RelacionSimilitud.java` - peso, generosComunes

### ✅ Repository
- `PeliculaRepository.java` - Queries para Neo4j

### ✅ Controller
- `PeliculaController.java` - Endpoints REST

### ✅ Frontend
- `index.html` - Interfaz web

### ✅ Configuración
- `application.properties` - **⚠️ EDITAR CON TUS CREDENCIALES**

---

## 🎯 Endpoints

```
GET  /api/peliculas              - Todas
GET  /api/peliculas/{id}         - Por ID
GET  /api/peliculas/genero/{nombre} - Por género
GET  /api/peliculas/top          - Top rating
GET  /api/peliculas/{id}/relacionadas - Relacionadas
GET  /api/peliculas/test         - Test
```


**¡Éxito con el TP!** 🚀