<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <!-- <#include "nav-bar.ftl" /> Not sure if the nav bar is needed, possibly for a back/home button in case you don't want to sign in. -->

  <div class="body">

    
    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <form action="./signin" method="POST">
        Username:
        <br/>
        <input name="userName" />
        <br/><br/>
        <button type="submit">Ok</button>
      </form>

  </div>

</div>
</body>

</html>
