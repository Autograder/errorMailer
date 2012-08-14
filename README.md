errorMailer
===========
    
Play! One module. Sends you an email with detailed information to any exception raised in a controller or a template and includes information about the request, params, cookies, the renderArgs and some other things.

So you can see the error, look at the possibly incorrect sorce code or the possibly missing/wrong params, set some breakpoints, call the same url the user called and start to step debug the application.

[Example of an email send with errorMailer][1]

Some of the informations you will see on 500-response:

    Sourcecode like you already know from 500-responses in dev mode
    Informations about session
    params
    headers
    renderArgs
    MemoryUsage
    CPU load
    contentType
    encoding
    request.isNew()
    request.isAjax()
    serverhost
    remoteadress (usefully if you use a (reverse-)proxy
    ...some more
 
#Step 1: Add the errorMailer to the dependencies.
        
     # Application dependencies

    require:
        - play
        - maklemenz -> errorMailer 0.1.6
        
        
    repositories:
         - maklemenz:
            type: http
            artifact:  https://github.com/downloads/maklemenz/[module]/[module]-[revision].zip
            contains:
            - maklemenz -> *
        
Or using my dropbox (deprecated)

    # Application dependencies

    require:
        - play
        - mkmod -> errorMailer 0.1.6
        
    repositories:
         - mkmod:
            type: http
            artifact: http://dl.dropbox.com/u/17394524/playmodules/[module]-[revision].zip
            contains:
            - mkmod -> *
        

    
#Step 2: Set the required configuration:

Minimal setup:

    #application.conf
    errormailer.from = appName <appname@app.net>
    errormailer.to = you <you@you.de>, admin@asd.de, myMail+errors@xd.com


All configuration options

    #application.conf

    #OPTIONAL DUE DEFAULTS
    #boolean, default true
    errormailer.sendonprod = true
    #boolean, default false
    errormailer.sendondev = true

    #REALLY NEEDED: the email adresses
    #from-email-adress, you NEED to set this. 
    errormailer.from = appName <appname@app.net>
    #comma seperated list of target email addresses. you NEED to set this
    errormailer.to = you <you@you.de>, admin@asd.de, myMail+errors@xd.com

    #FULLY OPTIONAL: limitations
    #{boolean true | false -> default false}
    errorMailer.limit.enabled = true
    #{int number of emails per timeunit -> NO default}
    errorMailer.limit.count = 10
    #{int timeunit in minutes -> NO default}
    errorMailer.limit.minutes = 30

    #FULLY OPTIONAL: exclusions
    errorMailer.exclusionsenabled = true
    errorMailer.exclusion.MyNiceRegexName = a regex
    errorMailer.exclusion.AnotherRegex = another nice regex
    #the regexes are checked against a String builded like this: request.action + request.path + e.getClass().getName() + e.getMessage();

#Step 3: Annotate the controller
If all your controller extends a kind of BaseController.class, you just have to annotate this BaseController, if not you have to annotate any controller with

    @With(ErrorMailer.class)


  [1]: http://dl.dropbox.com/u/17394524/playmodules/errorMailer.example.html