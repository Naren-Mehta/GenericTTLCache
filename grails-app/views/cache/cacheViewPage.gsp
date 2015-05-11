<html>
<head>
    <meta name="layout" content="main"/>
    <title>Generic TTL Cache</title>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="col-md-2"></div>

            <div class="col-md-8">
                <g:form controller="cache" action="calculateTTLCache" class="form-horizontal"
                        enctype="multipart/form-data" method="POST">

                    <div class="form-group text-left">
                        <h2>Generic TTL Cache Parameters</h2>

                        <g:render template="/common/aboutUserDetails"/>
                        <a href="#" role="button" class="btn btn-info"
                           data-target="#aboutUserDetails"
                           data-toggle="modal">About</a>
                    </div>


                    <div class="form-group">
                    %{--<div class="col-sm-6 col-sm-offset-2">--}%
                        <g:hasErrors bean="${simulateCO}">
                            <ul class="alert alert-danger" role="alert" style="list-style: none">
                                <g:eachError bean="${simulateCO}" var="error">
                                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                                            error="${error}"/></li>
                                </g:eachError>
                            </ul>
                        </g:hasErrors>
                        <g:if test="${flash.message}">
                            <ul class="alert alert-danger" role="alert">
                                ${flash.message}
                            </ul>
                        </g:if>
                        ${flash.clear()}

                    %{--</div>--}%
                    </div>

                    <div class="form-group">
                        <p class="h4">Enter Path of Cache file (e.g. C:\cache.txt)</p>
                        <input type="file" name="cacheFile">

                    </div>

                    <div class="form-group">
                        <p class="h4">Enter the size (number of entries) of the Cache
                        </p>

                        <g:select name="cacheSize" class="form-control" from="${["16", "32", "64", "128", "256"]}"
                                  noSelection="['': '-select Cache Size-']" value="${simulateCO?.cacheSize}"/>
                    </div>

                    <div class="form-group">
                        <p class="h4">Cache Update rate (Refresh Rate - Seconds)</p>

                        <g:select name="cacheRefreshRate" class="form-control"
                                  from="${["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]}"
                                  noSelection="['': '-select Cache Refresh Rate-']"
                                  value="${simulateCO?.cacheRefreshRate}"/>
                    </div>

                    <div class="form-group">
                        <p class="h4">Enter Path of sample (IP log) file (e.g. C:\sample.txt)
                        </p>

                        <input type="file" name='userFiles[]' multiple>

                    </div>

                    <div class="form-group">
                        <p class="h4">Sample (IP log) file Update rate (Refresh Rate - Seconds). </p>

                        <g:select name="checkNextSampleRate" class="form-control"
                                  from="${["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]}"
                                  noSelection="['': '-select Cache Replacement Policy-']"
                                  value="${simulateCO?.checkNextSampleRate}"/>
                    </div>

                    <div class="form-group">
                        <p class="h4">Number of entries to read from sample file</p>

                        <g:select name="numberOfEntries" class="form-control"
                                  noSelection="['': '-select Batch size -']"
                                  value="${simulateCO?.numberOfEntries}"
                                  from="${["100", "500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"]}"/>

                    </div>

                    <div class="form-group">
                        <p class="h4">Log file Update rate (Seconds)</p>

                        <g:select name="updateLOGFILERate" class="form-control"
                                  noSelection="['': '-select LOG FILE refresh rate -']"
                                  value="${simulateCO?.updateLOGFILERate}"
                                  from="${["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]}"/>

                    </div>

                    <div class="form-group">
                        <p class="h4">Cache Replacement Schemes: 0 for LRU and 1 for FIFO</p>


                        <g:select name="replacingScheme" class="form-control"
                                  noSelection="['': '-select File selection menu -']"
                                  value="${simulateCO?.replacingScheme}"
                                  from="${["0", "1"]}"/>

                    </div>


                    <div class="form-group">
                        <input type="submit" class="btn btn-primary btn-lg" value="Calculate"/>
                    </div>
                </g:form>

            </div>

            <div class="col-md-2"></div>
        </div>
    </div>
</div>
</body>
</html>
