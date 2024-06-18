



(config
 (text-field
  :name "clientId" ; name is fixed
  :label "Enter clientId"
  :placeholder "")
 (password-field
  :name "clientSecret" ; name is fixed
  :label "Enter clientSecret"
  :placeholder "")
 (oauth2/authorization-code-flow-client-credentials
  (token
   (source
    (http/post
     url:"https://api.paychex.com/auth/oauth/v2/token"
     (body-params
      "response_type" "code"
      "client_id" "${CLIENT-ID}"
      "client_secret" "${CLIENT-SECRET}"
    ;;   "scope" "contact_read all_contact_read""
    "grant_type " " authorization_code "
                                       )))
   (fields
    access-token :<= "access_token"
    token-type :<= "token_type"
    scope :<= "scope" 
    expires-in :<= "expires_in")))
 
)
(default-source
 (http/get :base-url "https://api.paychex.com"
           (header-params "Accept" "application/json")) 
 (auth/oauth2)
 (paging/no-pagination)
 (error-handler
  (when :status 401 :action refresh)
    (when :status 402 :action fail)
    (when :status 403 :action fail)
  ))
    
(entity COMPANY
        (api-docs-url "https://developer.paychex.com/documentation#tag/Company")
        (source (http/get :url "/companies")
                
                ) 
        )