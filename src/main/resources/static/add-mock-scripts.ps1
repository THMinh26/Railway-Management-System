# Script để thêm config.js và mock-api.js vào tất cả HTML files

$htmlFiles = Get-ChildItem -Path "." -Filter "*.html" -Exclude "test-mock.html", "TEMPLATE.html", "INTEGRATION_NOTES.html", "index_backup.html"

foreach ($file in $htmlFiles) {
    Write-Host "Processing $($file.Name)..."
    
    $content = Get-Content $file.FullName -Raw
    
    # Check if already has scripts
    if ($content -notmatch "mock-api\.js") {
        # Find </head> tag
        if ($content -match "</head>") {
            # Add scripts before </head>
            $newContent = $content -replace "</head>", @"
    <script src="js/config.js"></script>
    <script src="js/mock-api.js"></script>
</head>
"@
            
            Set-Content -Path $file.FullName -Value $newContent -NoNewline
            Write-Host "  ✅ Added scripts to $($file.Name)"
        } else {
            Write-Host "  ⚠️ No </head> tag found in $($file.Name)"
        }
    } else {
        Write-Host "  ℹ️ Scripts already present in $($file.Name)"
    }
}

Write-Host "`n✅ Done! All HTML files updated."
