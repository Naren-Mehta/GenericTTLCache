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
                        <h2>Welcome to the Generic TTL Cache</h2>
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
                        <p class="h4">Enter the name/path of Cache file ( e.g. cache.txt )</p>
                        <input type="file" name="cacheFile">

                    </div>

                    <div class="form-group">
                        <p class="h4">Enter the size ( number of entries ) of the Cache
                        </p>

                        <g:select name="cacheSize" class="form-control" from="${["16", "32", "64", "128", "256"]}"
                                  noSelection="['': '-select cache type-']" value="${simulateCO?.cacheSize}"/>
                    </div>

                    <div class="form-group">
                        <p class="h4">How Often do you want to update CacheFile ( Cache Refresh Rate - Seconds )</p>

                        <g:select name="cacheRefreshRate" class="form-control"
                                  from="${["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]}"
                                  noSelection="['': '-select cache Refresh Rate-']"
                                  value="${simulateCO?.cacheRefreshRate}"/>
                    </div>

                    <div class="form-group">
                        <p class="h4">Enter the first file from which program will start reading data (e.g. sample1.txt )
                        </p>

                        <input type="file" name='userFiles[]' multiple>

                    </div>

                    <div class="form-group">
                        <p class="h4">How Often do you want to check next Sample file in the sequence ( seconds )</p>

                        <g:select name="checkNextSampleRate" class="form-control"
                                  from="${["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]}"
                                  noSelection="['': '-select Replacement policy-']"
                                  value="${simulateCO?.checkNextSampleRate}"/>
                    </div>

                    <div class="form-group">
                        <p class="h4">Number of entries to read from Sample file AT A TIME</p>


                        <g:select name="numberOfEntries" class="form-control"
                                  noSelection="['': '-select number Of Entries -']"
                                  value="${simulateCO?.numberOfEntries}"
                                  from="${["100", "500", "1000", "1500", "2000", "2500", "3000", "3500", "4000", "4500", "5000"]}"/>

                    </div>

                    <div class="form-group">
                        <p class="h4">How Often do you want update LOG FILE ( seconds )</p>


                        <g:select name="updateLOGFILERate" class="form-control"
                                  noSelection="['': '-select update LOG FILE Rate -']"
                                  value="${simulateCO?.updateLOGFILERate}"
                                  from="${["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]}"/>

                    </div>

                    <div class="form-group">
                        <p class="h4">Enter Replacing Sceheme . 1 for LRU and 2 for FIFO</p>


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
