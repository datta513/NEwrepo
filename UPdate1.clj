(config
 (text-field
  :name "clientId" 
  :label "Enter clientId" 
  :placeholder "Enter Client Id")
 (password-field 
  
  :name "clientSecret"
  :label "Enter clientSecret" 
  :placeholder "") 
 
 (oauth2/client-credentials 
  (access-token
   (source
       (http/post
        url:"https://api.paychex.com/auth/oauth/v2/token"
        (body-params
         "response_type" "code"
         "client_id" "${CLIENT-ID}"
         "client_secret" "${CLIENT-SECRET}" 
       "grant_type " "client_credentials"
                                          ))))
      (fields
       access_token :<= "access_token"
       token_type :<= "token_type"
       scope :<= "scope" 
       expires_in :<= "expires_in")) 
 
 (default-source
  (http/get :base-url "https://api.paychex.com"
            (header-params "Accept" "application/json"))
  (auth/oauth2)
  (paging/no-pagination)
  (error-handler
   (when :status 401 :action refresh)
   (when :status 429 :action red-limit)
   
   ))
 
 (entity COMPANY
         (api-docs-url "https://developer.paychex.com/documentation#tag/Company")
         (source (http/get :url "/companies")
                 (query-params "displayid" {"displayid"})))
)