        <nav class="navbar navbar-expand navbar-light bg-blue-gradient topbar mb-4 static-top shadow">

          <!-- Sidebar Toggle (Topbar) -->
          <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
            <i class="fa fa-bars"></i>
          </button>
          <div>
            <b class="text-white">Selamat datang, <s:property value="%{#session.LOGIN_KEY.userName}" /></b>
          </div>

          <!-- Topbar Navbar -->
          <ul class="navbar-nav ml-auto">
            <li >
              <s:a action="Logout">
                <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                <b class="text-white">Logout</b>
              </s:a>
            </li>
          </ul>

        </nav>