import requests
import json

# GraphQL endpoint
graphql_endpoint = 'http://127.0.0.1:5000/graphql'  # Replace with your actual GraphQL endpoint
# INSERT USER
# graphql_query='''
#   mutation {
#     createUser(nama:"test",email:"test",password:"test",nomor_telepon:"test",
#     tgl_lahir:"2022-2-2",nik:"test",pengalaman:1,pengalaman_pro:1,edukasi:"Undergraduate",
#     url_photo:"test",deskripsi:"test",stream:"Spesialisasi") {
#       user {
#         nama
#         email
#         password
#         nomor_telepon
#         tgl_lahir
#         nik
#         pengalaman
#         pengalaman_pro
#         edukasi
#         url_photo
#         deskripsi
#         stream
#       }
#       errors
#     } 
#   }
# '''
# QUERY USER
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
# INSERT COMPANY
# graphql_query = '''
#   mutation {
#     createCompany(nama:"test",alamat:"test",email:"test",password:"test",url_photo:"test",deskripsi:"test") {
#       company {
#         nama
#         alamat
#         email
#         password
#         url_photo
#         deskripsi
#       }
#       success
#       errors
#     }
#   }
# '''
# QUERY COMPANY LOGIN
# graphql_query = '''
#   query {
#     cekLoginCompany(email:"test",password:"test") {
#       success
#       errors
#       company {
#         nama
#         alamat
#         email
#         password
#         url_photo
#         deskripsi
#       }
#     }
#   }
# '''
# INSERT SKILL
# graphql_query = '''
#   mutation {
#      createSkills(nama:"HTML") {
#        success
#        errors
#        skills {
#          id
#          nama
#         }
#      } 
#    }
# '''
# INSERT PENGALAMAN
# graphql_query = '''
#   mutation {
#      createPengalaman(nama_pekerjaan: "P Dev", tgl_mulai: "2022-2-2", tgl_selesai: "2022-2-2", tmpt_bekerja: "BCA", pkrjn_profesional: true, user_iduser: 2) {
#        success
#        errors
#        pengalaman {
#         id
#         nama_pekerjaan
#         tgl_mulai
#         tgl_selesai
#         tmpt_bekerja
#         pkrjn_profesional
#         user_iduser
#         }
#      } 
#    }
# '''
# INSERT EDUKASI
# graphql_query = '''
#   mutation {
#      createEdukasi(nama_institusi: "tes2", jenjang: "Master", tgl_awal: "2022-2-2", tgl_akhir: "2022-2-2", deskripsi: "test", user_iduser: 2) {
#        success
#        errors
#        edukasi {
#         id
#         nama_institusi
#         jenjang
#         tgl_awal
#         tgl_akhir
#         deskripsi
#         user_iduser
#         }
#      } 
#    }
# '''
# QUERY EDUKASI
# graphql_query = '''
#   query {
#      listEdukasiUser(user_iduser: 1) {
#        edukasi {
#         id
#         nama_institusi
#         jenjang
#         tgl_awal
#         tgl_akhir
#         deskripsi
#         user_iduser
#         }
#      } 
#    }
# '''
# QUERY PENGALAMAN
# graphql_query = '''
#   query {
#      listPengalamanUser(user_iduser: 1) {
#        pengalaman {
#           id
#           nama_pekerjaan
#           tgl_mulai
#           tgl_selesai
#           tmpt_bekerja
#           pkrjn_profesional
#           user_iduser
#         }
#      } 
#    }
# '''

# QUERY USER SKILLS
# graphql_query = '''
#   mutation {
#     createUserHasSkills(user_iduser: 2, skills_id: 3) {
#     success
#     errors
#       user_has_skills {
#         user_iduser
#         skills_id
#       }
#     } 
#   }
# '''
# QUERY USER SKILLS
# graphql_query = '''
#   query {
#     listUserSkills(user_iduser: 1) {
#     success
#     errors
#       skills {
#         id
#         nama
#       }
#     } 
#   }
# '''
# INSERT LOKER
# graphql_query = '''
#   mutation {
#     createLowongan(nama:"test", deskripsi:"test", jmlh_butuh:100, company_id:1, url_photo:"test") {
#     success
#     errors
#     lowongan {
#     nama
#     deskripsi
#     jmlh_butuh
#     company_id
#     url_photo
#     }
#     } 
#   }
# '''
# graphql_query = '''
#   mutation {
#     createSkillRequired(skills_id: 1, lowongan_id: 1) {
#     success
#     errors
#     skills_required {
#       skills_id
#       lowongan_id
#     }
#     } 
#   }
#  '''

graphql_query = '''
  mutation {
    createSkillRequired(skills_id: 1, lowongan_id: 1) {
    success
    errors
    skills_required {
      skills_id
      lowongan_id
    }
    } 
  }
 '''
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
