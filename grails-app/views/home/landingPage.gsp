<html>
<head>
    <meta name="layout" content="main"/>
    <title>TTL Caches: generic and JDBC</title>
</head>

<body>
<div class="container">
    <div class="row">

        <div class="form-group text-center">
            <h2> TTL IP Caches - Generic, JDBC</h2>

        </div>


        <div class="span4">
            <div class="">
                <dl class="dl-horizontal">
                    <dt>Goal</dt>
                    <dd>TTL cache stores data so that future requests for that data can be served faster. The data that is stored within a
                    cache are e-mail senders (IP addresses) that have been extracted from a syslog file. If requested data is contained
                    in the cache (cache hit), this request can be served by simply reading the cache, which is comparatively faster. TTL
                    specifies how long a cache is supposed to use the entry before it expires and a new one needs to replace that.
                    </dd>
                </dl>
                <dl class="dl-horizontal">
                    <dt>Architecture</dt>
                    <dd>Cache simulator are of following types
                        <div>  <br>    </div>

                        <div class="span4">
                            <div class="">
                                <dl class="dl-horizontal">
                                    <dt>Generic</dt>
                                    <dd> Works with tables in text files
                                    </dd>
                                </dl>
                                <dl class="dl-horizontal">
                                    <dt>JDBC</dt>
                                    <dd>  Works with database tables such as
                                    mySQL,
                                    Oracle,
                                    Excel
                                    </dd>
                                </dl>
                            </div>
                        </div>

                    </dd>
                </dl>
                <dl class="dl-horizontal">
                    <dt>Process</dt>
                    <dd> In this simulator, in the start we will read a sample.txt and find if the entries are in cache.txt.</dd>
                    <dd>If the entries are not present in the cache.txt (a miss), we store (or update) them in the cache.txt</dd>
                    <dd>We keep track of hits and misses and bandwidth and latency involved.</dd>
                </dl>


                <dl class="dl-horizontal">
                    <dt>Memory Trace</dt>
                    <dd>IP - TTL List</dd>
                    <dd>TS - IP - TTL list</dd>
                </dl>

            </div>
        </div>

      %{--  <div class="form-group text-center">
            <img src="${resource(dir: "images/project", file: "one.jpg")}"/>
        </div>


        <div class="form-group text-center">
           <img src="${resource(dir: "images/project", file: "two.jpg")}"/>
        </div>--}%



        <div class="col-md-12">
            <div class="col-md-2"></div>

            <div class="col-md-8">

                <div class="form-group text-center">
                    <g:link controller="cache" action="cacheViewPageWithDBChoice"
                            class="btn btn-primary btn-lg">ENTER DATABASE CHOICE</g:link>
                </div>

                <div class="form-group text-center">
                    <g:link controller="cache" action="cacheViewPage"
                            class="btn btn-primary btn-lg">Enter TTL IP Cache Parameters</g:link>
                </div>

            </div>

            <div class="col-md-2"></div>

        </div>
    </div>
</div>
</body>
</html>