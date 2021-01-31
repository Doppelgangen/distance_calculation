<html>
<body>
<h2>
    <a>Hello there!</a>
</h2>
<h4>
    <a href="/rest/cities">Display list of all cities</a>
</h4>
<br/>
<form action="/rest/distance" method="post">
    <h4>Choose measurements</h4>
    <select size="3" name="calculationType">
        <option selected>Crowflight</option>
        <option>Distance Matrix</option>
        <option>All</option>
    </select>
    <br/>
    <p>
    City from : <input type="text" name="cityFrom"/>
    </p>
    <p>
    City to : <input type="text" name="cityTo"/>
    </p>
    <button type="submit" >Choose</button>
</form>
</body>
</html>
