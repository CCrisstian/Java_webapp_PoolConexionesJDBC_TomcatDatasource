<jsp:include page="layout/header.jsp"/>
<h3>${title}</h3>
<ul class="list-group" style="max-width: 400px;"> <!-- Ajusta el tamaño máximo aquí -->
    <li class="list-group-item active">Menu de Opciones</li>
    <li class="list-group-item"><a href="${pageContext.request.contextPath}/usuarios">Usuarios</a></li>
    <li class="list-group-item"><a href="${pageContext.request.contextPath}/productos">Productos</a></li>
    <li class="list-group-item"><a href="${pageContext.request.contextPath}/login.html">Login</a></li>
    <li class="list-group-item"><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
    <li class="list-group-item"><a href="${pageContext.request.contextPath}/carro/ver">Ver Carro</a></li>
</ul>
<jsp:include page="layout/footer.jsp"/>