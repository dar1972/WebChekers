<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />
  
  <div class="body">

    
    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <h2> Players Online </h2>
    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->
    <#if currentUser??>
      <#if lobbySize == 1>
        There are no other players online at this time.
      <#else>
        <form action="./" method="POST">
          <#list userList?keys as key>

          <!--<a href= '/game?opponentName=${key}'> ${key} </a> -->

    
          <input type = "radio" id = ${key} name="opponentName" value = ${key} />
          <label for=${key}>${key}</label>
          <br/>
          
          </#list>

          <#if lobbySize gt 1>
            <br/>
            <button type="submit">Enter Game</button>
          </#if>
        </form>
      </#if>
    <#else>
      <#if lobbySize == 0>
        There are no other players online at this time.
      <#elseif lobbySize == 1>
        There is 1 person online. Sign in to join a game!
      <#else>
        There are ${lobbySize} players online. Sign in to join a game!
      </#if>
    </#if>

  </div>

</div>
</body>

</html>
