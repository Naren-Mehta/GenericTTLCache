<div class="container">

    <nav class="navbar navbar-inverse navbar-static-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <g:link controller="home" action="landingPage" class="navbar-brand">Simulate</g:link>
            </div>

            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <li><g:link controller="home" action="landingPage" class="active">Home</g:link></li>


                    %{--<sec:ifLoggedIn>--}%
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button"
                           aria-expanded="false"><um:userFullName/> <span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu">
                            <li><g:link controller="registration"
                                        action="viewUpdateProfile">Account Settings</g:link></li>
                            %{--<li class="divider"></li>--}%
                            <li><g:link controller="logout">Logout</g:link></li>
                        </ul>
                    </li>
                    %{--</sec:ifLoggedIn>--}%

                    %{--<sec:ifNotLoggedIn>--}%
                    <li><g:link controller="simulate">Simulate</g:link></li>

                    <li><g:link controller="login" action="auth">Login</g:link></li>
                    <li><g:link controller="registration" action="register">Register</g:link></li>
                    %{--</sec:ifNotLoggedIn>--}%

                </ul>
            </div>
        </div>
    </nav>
</div>
