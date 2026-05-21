import { serve } from "https://deno.land/std@0.168.0/http/server.ts"

serve(async (req) => {
  const body = await req.json()

  const serviceAccount = {
    project_id: "traveltracker-ee61d",
    client_email: "firebase-adminsdk-fbsvc@traveltracker-ee61d.iam.gserviceaccount.com",
    private_key: "-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDV7FQs1+u6eoQE\nK0cgyQh8+E908L5HPXNlOUciNguxcj/RV7uA4Qzeop+a4betbZ4Ptv3WLQPt+sk7\n7R6+ZzWJguf3YNT98tt2oEuI+vD8szO8JQriG1OWelFZyjxVY0X26o5pZaEATF6L\nZ24G0uPAwFkF9bE48QdUZg4Wa5xqu2Azlh8I617pWq1QKGo9isanIXMm+2WUL2WL\n2p8j9/qoj10IIRZTbhgfDVE7omgGEITpSgnjg9DVCCo5CWBnTJVBLyVRu47vLu0P\n0sunyPFKvnLQ14RJonRh4BJarV++0LFeF2k6Toup2utoly+39l6ot9IeZzuHZStQ\nA0XnJ8fpAgMBAAECggEAMj7E8pdwfadDFLuZhEDTpo6oEv5soszzKqZOZ+Gr02zT\nQndJyhaR8ETgGf/EJY7mnihL7J88GgxacaHbI7uYHolJtJfgzxjYpT83HqBaM/Bf\nSWSl7r+eXWyzaNSQYTnk9/7VnjnJnAlT94g+lom4BtDm1/iCPK6YGyGxU2DJJ2QX\nYLt585ec9VPOQUdS2H/tJOqMU43vJpLC9CpcL9NLOrfO0XrotS8sWZTYWBNgYzmP\ntLmGhz5ABurBa7Be9ciGwYnNb/DZkLJMQ6E/KhEFESeRuETj6LXOg40CkhSyrcYG\nrU5iYIWVDc6dQEg0h6yc4SqIepq8LcjXJG7AAOX0xQKBgQDq5qcTXm66nfwxaxGO\noN30X/7cqQ2GIVzhO+i8GpfSL9FGsM87vNHXCg+Vc2VOkcIyCs5OnMOo+kotQ6rJ\njWfVuDDi3aJBFTaFkSCoJ+6gOkGvrwtq7EKr9P4Z/4VRj5bGI18VxwaIPNW8awvo\n13KkhDM4RmRhxgpmuqL8lmKYcwKBgQDpI08YMBtofZTwytYHRWqWF9eDA+/bWU/a\n/nK0SLK/FMo8TgLNzC47UK0kKMTu5wKncwKndMWn8XJtm+pHQL9vn1X8+UZMi47I\nJ7UiuqxhOg3aRLIJErS64b1UhfGCixV01AXhnPWH70d9eTiCAIKWRf07JRvM9RwW\nTapR76mzMwKBgBg0VijvxRcjqlXpQ3Df8J76uUdBgCw/CdQHmU6wqs40sSrIk0ID\njdfLqJlsv5ad/FWtuV5uVtb1m2kYnOOR1441IYxu7AYqymhu8N+C5drKxfP1ZIbK\n+3++ieRh6PnjgtS91lFnJawqIJf+q8Z9VMnQ+Xol/rf4cR/uwVanpFczAoGAPMm4\nFdZ1TwlvjFlW9Q5kfD47My4TnF3tNGyj+934vVnuYUr18DMM2upuOr9mmeQiDQcv\n6fnIj8Gk1G+S5oE7aR9ijFNywQKieA3ibOu/hppcgkznHvaBqTiy5RcHocIiFhQy\ndGE4o/j/6vdwhHUdl5EoNgO2+/ntGOOnRTX4SYECgYA3fJ9pW71IRGbwP0yNCVPy\niIjFMA2gS8n0Fcl51nE8P7zun51biUjIf6sUTmgGKp/MzsIit2hfNVhhkhas47BS\n+AjIlHbve66b+LOITz2K+em085Kk9e01UQpSTUzFI+N7F8EFR+Pc9lx7qNeKcY2H\ntyBg6QJMjdqxNtlZXA8dwA==\n-----END PRIVATE KEY-----\n"
  }

  const accessToken = await getAccessToken(serviceAccount)

  const response = await fetch(
    `https://fcm.googleapis.com/v1/projects/${serviceAccount.project_id}/messages:send`,
    {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${accessToken}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        message: {
          token: body.token,
          notification: {
            title: body.title,
            body: body.message
          }
        }
      })
    }
  )

  const result = await response.text()
  return new Response(result, { status: response.status })
})

async function getAccessToken(serviceAccount: any): Promise<string> {
  const now = Math.floor(Date.now() / 1000)
  const payload = {
    iss: serviceAccount.client_email,
    scope: "https://www.googleapis.com/auth/firebase.messaging",
    aud: "https://oauth2.googleapis.com/token",
    iat: now,
    exp: now + 3600
  }

  const jwt = await signJWT(payload, serviceAccount.private_key)

  const tokenRes = await fetch("https://oauth2.googleapis.com/token", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: `grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=${jwt}`
  })

  const tokenData = await tokenRes.json()
  return tokenData.access_token
}

async function signJWT(payload: any, privateKeyPem: string): Promise<string> {
  const header = { alg: "RS256", typ: "JWT" }

  const enc = new TextEncoder()
  const headerB64 = btoa(JSON.stringify(header)).replace(/=/g, "").replace(/\+/g, "-").replace(/\//g, "_")
  const payloadB64 = btoa(JSON.stringify(payload)).replace(/=/g, "").replace(/\+/g, "-").replace(/\//g, "_")
  const signingInput = `${headerB64}.${payloadB64}`

  const pemContents = privateKeyPem
    .replace(/-----BEGIN PRIVATE KEY-----/, "")
    .replace(/-----END PRIVATE KEY-----/, "")
    .replace(/\n/g, "")

  const binaryDer = Uint8Array.from(atob(pemContents), c => c.charCodeAt(0))

  const privateKey = await crypto.subtle.importKey(
    "pkcs8",
    binaryDer.buffer,
    { name: "RSASSA-PKCS1-v1_5", hash: "SHA-256" },
    false,
    ["sign"]
  )

  const signature = await crypto.subtle.sign(
    "RSASSA-PKCS1-v1_5",
    privateKey,
    enc.encode(signingInput)
  )

  const signatureB64 = btoa(String.fromCharCode(...new Uint8Array(signature)))
    .replace(/=/g, "").replace(/\+/g, "-").replace(/\//g, "_")

  return `${signingInput}.${signatureB64}`
}