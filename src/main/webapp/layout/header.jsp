<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <style>
        .nav-link:hover {
            color: white; /* Cambia el color del texto al pasar el ratón */
            background-color: #007bff; /* Cambia el color de fondo al pasar el ratón */
        }

        .list-group-item a:hover {
            color: white; /* Cambia el color del texto al pasar el ratón */
            background-color: #007bff; /* Cambia el color de fondo al pasar el ratón */
            text-decoration: none; /* Elimina el subrayado */
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container-fluid">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/index.jsp">Home</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/productos">Productos</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
        </li>
        <li class="nav-item">
           <a class="nav-link" href="${pageContext.request.contextPath}/carro/ver">Ver Carro(${carro.items.size()})</a>
        </li>
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            ${not empty sessionScope.username? sessionScope.username: "Cuenta"}
          </a>
          <ul class="dropdown-menu">
            <li>
                <a class="dropdown-item"
                href="${pageContext.request.contextPath}/${not empty sessionScope.username? "logout": "login"}">
                    ${not empty sessionScope.username? "Logout": "Login"}
                </a>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>
<div class="container">