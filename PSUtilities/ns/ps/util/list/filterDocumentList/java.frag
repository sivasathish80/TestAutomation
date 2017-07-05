<?xml version="1.0" encoding="UTF-8"?>

<Values version="2.0">
  <value name="name">filterDocumentList</value>
  <array name="sig" type="value" depth="1">
    <value>[i] record:1:required originalList</value>
    <value>[i] record:1:required filter</value>
    <value>[i] - field:0:required columnName</value>
    <value>[i] - field:1:required columnValues</value>
    <value>[i] - field:0:optional operator {"equals","equalsIgnoreCase","=","&gt;","&lt;","&gt;=","&lt;=","max","min"}</value>
    <value>[i] record:1:optional columnTypes</value>
    <value>[i] - field:0:required name</value>
    <value>[i] - field:0:required type {"String","Numeric","Date"}</value>
    <value>[i] - field:0:optional pattern</value>
    <value>[o] record:1:required filteredList</value>
  </array>
  <value name="subtype">unknown</value>
  <value name="sigtype">java 3.5</value>
  <value name="encodeutf8">true</value>
  <value name="body">SURhdGFDdXJzb3IgY3Vyc29yID0gcGlwZWxpbmUuZ2V0Q3Vyc29yKCk7DQpJRGF0YVtdIG9yaWdp
bmFsTGlzdCA9IElEYXRhVXRpbC5nZXRJRGF0YUFycmF5KGN1cnNvciwgIm9yaWdpbmFsTGlzdCIp
Ow0KSURhdGFbXSBmaWx0ZXIgPSBJRGF0YVV0aWwuZ2V0SURhdGFBcnJheShjdXJzb3IsICJmaWx0
ZXIiKTsNCklEYXRhW10gY29sdW1uVHlwZXMgPSBJRGF0YVV0aWwuZ2V0SURhdGFBcnJheShjdXJz
b3IsICJjb2x1bW5UeXBlcyIpOw0KaWYgKG9yaWdpbmFsTGlzdCA9PSBudWxsIHx8IGZpbHRlciA9
PSBudWxsKQ0Kew0KICByZXR1cm47DQp9DQoNCkFycmF5TGlzdCBsaXN0ID0gbmV3IEFycmF5TGlz
dCgpOw0KDQpvdXRlcjogZm9yIChpbnQgaSA9IDA7IGkgPCBvcmlnaW5hbExpc3QubGVuZ3RoOyBp
KyspDQp7DQogIElEYXRhQ3Vyc29yIGxpc3RDdXJzb3IgPSBvcmlnaW5hbExpc3RbaV0uZ2V0Q3Vy
c29yKCk7DQogIGJvb2xlYW4gbWF0Y2hlZCA9IHRydWU7DQogIGZvciAoaW50IGogPSAwOyBqIDwg
ZmlsdGVyLmxlbmd0aDsgaisrKQ0KICB7DQogICAgSURhdGFDdXJzb3IgZmlsdGVyQ3Vyc29yID0g
ZmlsdGVyW2pdLmdldEN1cnNvcigpOw0KICAgIFN0cmluZyBjb2x1bW5OYW1lID0gSURhdGFVdGls
LmdldFN0cmluZyhmaWx0ZXJDdXJzb3IsICJjb2x1bW5OYW1lIik7DQogICAgU3RyaW5nIG9wZXJh
dG9yID0gSURhdGFVdGlsLmdldFN0cmluZyhmaWx0ZXJDdXJzb3IsICJvcGVyYXRvciIpOw0KICAg
IGlmIChvcGVyYXRvciA9PSBudWxsKQ0KICAgIHsNCiAgICAgIG9wZXJhdG9yID0gImVxdWFscyI7
DQogICAgfQ0KDQogICAgU3RyaW5nW10gY29sdW1uVmFsdWVzID0gSURhdGFVdGlsLmdldFN0cmlu
Z0FycmF5KGZpbHRlckN1cnNvciwgImNvbHVtblZhbHVlcyIpOw0KICAgIFN0cmluZyBpdGVtVmFs
dWUgPSBJRGF0YVV0aWwuZ2V0U3RyaW5nKGxpc3RDdXJzb3IsIGNvbHVtbk5hbWUpOw0KICAgIFN0
cmluZ1tdIGNvbHVtbkluZm8gPSBnZXRDb2x1bW5JbmZvKGNvbHVtblR5cGVzLCBjb2x1bW5OYW1l
KTsNCg0KICAgIHRyeQ0KICAgIHsNCiAgICAgIGlmIChvcGVyYXRvci5lcXVhbHMoIm1heCIpIHx8
IG9wZXJhdG9yLmVxdWFscygibWluIikpDQogICAgICB7DQogICAgICAgIGludFtdIGluZGljZXMg
PSBnZXRNYXhNaW5WYWx1ZXNJbmRpY2VzKG9yaWdpbmFsTGlzdCwgY29sdW1uTmFtZSwgY29sdW1u
SW5mb1swXSwgY29sdW1uSW5mb1sxXSwgb3BlcmF0b3IpOw0KICAgICAgICBmb3IgKGludCBrID0g
MDsgayA8IGluZGljZXMubGVuZ3RoOyBrKyspDQogICAgICAgIHsNCiAgICAgICAgICBsaXN0LmFk
ZChvcmlnaW5hbExpc3RbaW5kaWNlc1trXV0pOw0KICAgICAgICB9DQogICAgICAgIGJyZWFrIG91
dGVyOw0KICAgICAgfQ0KDQogICAgICBpZiAoIW1hdGNoQW55KGl0ZW1WYWx1ZSwgY29sdW1uVmFs
dWVzLCBvcGVyYXRvciwgY29sdW1uSW5mb1swXSwgY29sdW1uSW5mb1sxXSkpDQogICAgICB7DQog
ICAgICAgIG1hdGNoZWQgPSBmYWxzZTsNCiAgICAgICAgYnJlYWs7DQogICAgICB9DQogICAgfQ0K
ICAgIGNhdGNoIChFeGNlcHRpb24gZSkNCiAgICB7DQogICAgICB0aHJvdyBuZXcgU2VydmljZUV4
Y2VwdGlvbihlKTsNCiAgICB9DQogIH0NCg0KICBpZiAobWF0Y2hlZCkNCiAgew0KICAgIGxpc3Qu
YWRkKG9yaWdpbmFsTGlzdFtpXSk7DQogIH0NCiAgbGlzdEN1cnNvci5kZXN0cm95KCk7DQp9DQoN
CklEYXRhW10gZmlsdGVyZWRMaXN0ID0gbnVsbDsNCg0KaWYgKGxpc3Quc2l6ZSgpID4gMCkNCnsN
CiAgZmlsdGVyZWRMaXN0ID0gbmV3IElEYXRhW2xpc3Quc2l6ZSgpXTsNCiAgbGlzdC50b0FycmF5
KGZpbHRlcmVkTGlzdCk7DQp9DQoNCklEYXRhVXRpbC5wdXQoY3Vyc29yLCAiZmlsdGVyZWRMaXN0
IiwgZmlsdGVyZWRMaXN0KTsNCmN1cnNvci5kZXN0cm95KCk7DQo=</value>
</Values>
