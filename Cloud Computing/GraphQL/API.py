import requests
import json

# GraphQL endpoint
graphql_endpoint = 'http://127.0.0.1:5000/graphql'  # Replace with your actual GraphQL endpoint

# GraphQL query or mutation
# graphql_query = '''
# {
#     query: {listPosts {success\nerrors\nposts\n{id\ntitle\ndescription\ncreated_at}}}
# }
# '''

graphql_query='''
  mutation {
    createUser(nama:"test",email:"test",password:"test",nomor_telepon:"test",
    tgl_lahir:"2022-2-2",nik:"test",pengalaman:1,pengalaman_pro:1,edukasi:"Undergraduate",
    url_photo:"test",deskripsi:"test",stream:"Spesialisasi") {
      user {
        nama
        email
        password
        nomor_telepon
        tgl_lahir
        nik
        pengalaman
        pengalaman_pro
        edukasi
        url_photo
        deskripsi
        stream
      }
      errors
    } 
  }
'''
# graphql_query='''
#   query {
#     listUsers {
#       success
#       errors
#       users{
#         iduser
#         nama
#         email
#         password
#         nomor_telepon
#         usercol
#         tgl_lahir
#         nik
#         pengalaman
#         pengalaman_pro
#         edukasi
#         url_photo
#         deskripsi
#         stream
#       }
#     }
#   }
# '''
#   query {
#     listPosts {
#       success
#       errors
#       posts {
#         id
#         title
#         description
#         created_at
#       }
#     }
#   }
# '''

# Headers (optional, include if needed)
headers = {
    'Content-Type': 'application/json'
}

# Make the GraphQL request
response = requests.post(graphql_endpoint, json={'query': graphql_query}, headers=headers)

# Check for successful response (status code 200)
if response.status_code == 200:
    # Parse and print the JSON response
    json_response = response.json()
    print(json.dumps(json_response, indent=2))
else:
    # Print error message for non-200 status codes
    print(f"Error: {response.status_code}, {response.text}")

