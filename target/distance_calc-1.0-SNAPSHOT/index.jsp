<html>
<head>
    <title>Distance calculation</title>
</head>

<body>
<h4>
    <a href="/rest/cities">Display list of all cities</a>
    <br/>
    <a href="/rest/cities/xml">Display list of all cities in XML</a>
</h4>
<br/>
<form action="/showDistance" method="post">
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
    <button type="submit">Show</button>
</form>
<br/>
<h4>Add new cities</h4>
<form action="/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file">
    <button type="submit">Submit</button>
</form>
</body>
</html>
